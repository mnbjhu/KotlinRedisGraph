import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import uk.gibby.redis.annotation.Relates
import uk.gibby.redis.attributes.ArrayAttribute
import uk.gibby.redis.attributes.RelationAttribute
import uk.gibby.redis.attributes.SerializableAttribute
import uk.gibby.redis.attributes.primative.BooleanAttribute
import uk.gibby.redis.attributes.primative.DoubleAttribute
import uk.gibby.redis.attributes.primative.LongAttribute
import uk.gibby.redis.attributes.primative.StringAttribute
import uk.gibby.redis.core.NodeResult
import uk.gibby.redis.core.RedisNode
import javax.lang.model.element.Element
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

fun processRedisNode(
    classElement: Element,
    classElements: List<Element>,
    relations: List<Triple<Relates, ClassName, TypeName>>
): FileSpec {
    val superClassName = RedisNode::class.asTypeName().parameterizedBy(classElement.asType().asTypeName())
    val nodeClassName = ClassName("uk.gibby.redis.generated", "${classElement.simpleName}Node")
    val resultClass = buildNodeClass(nodeClassName, superClassName, classElement, classElements, relations)
    return FileSpec.builder("uk.gibby.redis.generated", "${classElement.simpleName}Node")
        .addType(resultClass)
        .build()
}
fun TypeSpec.Builder.buildRelations(relations: List<Triple<Relates, ClassName, TypeName>>){
    relations.forEach {
        val first = ClassName("uk.gibby.redis.generated","${it.third}Node")
        val second = ClassName("uk.gibby.redis.generated","${try { it.first.to.asTypeName() } catch (e: MirroredTypeException){e.typeMirror.asTypeName()}}Node")
        val third = it.second
        addProperty(
            PropertySpec
                .builder(it.first.by, RelationAttribute::class.asTypeName().parameterizedBy(first, second, third))
                .initializer("relates(%T::class)", it.second)
                .build()
        )
    }
}

private fun buildNodeClass(
    nodeClassName: ClassName,
    nodeType: ParameterizedTypeName,
    clazz: Element,
    classElements: List<Element>,
    relations: List<Triple<Relates, ClassName, TypeName>>
): TypeSpec {
    val nodeClassBuilder = TypeSpec.classBuilder(nodeClassName)
        .superclass(nodeType)
    val members = clazz.enclosedElements.filter { it.kind.isField }
    members.forEach { nodeClassBuilder.addProperty(buildNodeProperty(it, classElements)) }
    nodeClassBuilder.addFunction(buildNodeGetResult(clazz.asType().asTypeName(), members))
    nodeClassBuilder.buildRelations(relations)
    return nodeClassBuilder.build()
}
fun buildNodeProperty(member: Element, classElements: List<Element>): PropertySpec {
    return PropertySpec
        .builder(member.simpleName.toString(), getNodeType(member.asType(), classElements))
        .delegate(getArrayAttributeFunction(member.asType(), classElements))
        .build()
}
fun getNodeType(type: TypeMirror, classElements: List<Element>): TypeName {
    if(classElements.any{ (it.asType().asTypeName().toString() == type.asTypeName().toString()) })
        return ClassName("uk.gibby.redis.generated", "${type.asTypeName()}Attribute")
    if(type.isEnum()) return SerializableAttribute::class.asClassName().parameterizedBy(type.asTypeName())
    when (type.asTypeName()) {
        String::class.asClassName(), javaString -> return StringAttribute::class.asClassName()
        Double::class.asClassName() -> return DoubleAttribute::class.asClassName()
        Long::class.asClassName(), javaLong -> return LongAttribute::class.asClassName()
        Boolean::class.asClassName() -> return BooleanAttribute::class.asClassName()
        else -> {}
    }
    return when{
        "^java\\.util\\.List<.*>$".toRegex().matches(type.asTypeName().toString()) -> {
            val innerType = (type as DeclaredType).typeArguments[0]
            val innerResult = getDefaultType(innerType, classElements)
            println(innerType.asTypeName())
            ArrayAttribute::class.asClassName()
                .parameterizedBy(toKotlin(innerType), innerResult)
        }
        else -> throw Exception("Type should be primitive, array or annotated with @RedisType. Found: '${type.asTypeName()}'")
    }
}
fun getArrayAttributeFunction(startType: TypeMirror, classElements: List<Element>): CodeBlock {
    var type = startType
    var levels = 0
    while("^java.util.List<.*>$".toRegex().matches(type.asTypeName().toString())){
        levels++
        val innerType = (type as DeclaredType).typeArguments[0]
        type = innerType
    }
    val builder = CodeBlock.builder()
    for (i in 0 until levels){
        if(i == 0) builder.add("%M(", MemberName("uk.gibby.redis.results", "arrayAttribute"))
        else builder.add("%M(", MemberName("uk.gibby.redis.results", "array"))
    }
    if(levels == 0) builder.add("%M()", getBaseAttributeFunction(type, classElements)) else builder.add("::%M", getBaseTypeFunction(type, classElements))
    for (i in 0 until levels){
        builder.add(")")
    }
    return builder.build()
}
fun getBaseAttributeFunction(type: TypeMirror, classElements: List<Element>): MemberName {
    if(type.isEnum()) return MemberName("uk.gibby.redis.core.ResultParent.Companion", "serializable")
    return when (type.asTypeName()) {
        ClassName("java.lang", "String"), javaString -> MemberName("uk.gibby.redis.core.ResultParent.Companion", "string")
        Long::class.asClassName(), javaLong -> MemberName("uk.gibby.redis.core.ResultParent.Companion", "long")
        ClassName("kotlin", "Double") -> MemberName("uk.gibby.redis.core.ResultParent.Companion", "double")
        ClassName("java.lang", "Boolean") -> MemberName("uk.gibby.redis.core.ResultParent.Companion", "boolean")
        in classElements.map { it.asType().asTypeName() } -> MemberName(
            "uk.gibby.redis.generated",
            "${type.asTypeName()}Attribute"
        )
        else -> throw Exception("Type should be primitive, array or annotated with @RedisType. '${type.asTypeName()}'")
    }
}
fun buildNodeGetResult(type: TypeName, members: List<Element>) =
    FunSpec.builder("getResult")
        .receiver(NodeResult::class)
        .addModifiers(KModifier.OVERRIDE)
        .addStatement("return %T(${members.joinToString { "${it.simpleName}.result()" }})", type)
        .returns(type)
        .build()

