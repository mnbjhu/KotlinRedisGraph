import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import uk.gibby.redis.results.*
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror



fun getType(member: Element, classElements: List<Element>): TypeName {
    return getDefaultType(member.asType(), classElements)
}

fun getBaseTypeFunction(type: TypeMirror, classElements: List<Element>): MemberName {
    if(type.isEnum()) return MemberName("uk.gibby.redis.core.ResultParent.Companion", "serializable")
    return when (type.asTypeName()) {
        ClassName("java.lang", "String"), javaString -> MemberName("uk.gibby.redis.core.ResultParent.Companion", "string")
        Long::class.asClassName(), javaLong -> MemberName("uk.gibby.redis.core.ResultParent.Companion", "long")
        ClassName("kotlin", "Double") -> MemberName("uk.gibby.redis.core.ResultParent.Companion", "double")
        Boolean::class.asClassName(), javaBool -> MemberName("uk.gibby.redis.core.ResultParent.Companion", "boolean")
        in classElements.map { it.asType().asTypeName() } -> MemberName(
            "uk.gibby.redis.generated",
            "${type.asTypeName().toString().split(".").last()}Result"
        )
        else -> throw Exception("Type should be primitive, array or annotated with @RedisType. '${type.asTypeName()}'")
    }
}

fun getArrayTypeFunction(startType: TypeMirror, classElements: List<Element>): CodeBlock {
    var type = startType
    var levels = 0
    while("^java.util.List<.*>$".toRegex().matches(type.asTypeName().toString())){
        levels++
        val innerType = (type as DeclaredType).typeArguments[0]
        type = innerType
    }
    val builder = CodeBlock.builder()
    for (i in 0 until levels){ builder.add("%M(", MemberName("uk.gibby.redis.results", "array")) }
    if(levels == 0) builder.add("%M()", getBaseTypeFunction(type, classElements)) else builder.add("::%M", getBaseTypeFunction(type, classElements))
    for (i in 0 until levels){
        builder.add(")")
    }
    return builder.build()
}

fun TypeMirror.isEnum() = kind == TypeKind.DECLARED && ((this as DeclaredType).asElement() as TypeElement).kind == ElementKind.ENUM
fun getDefaultType(type: TypeMirror, classElements: List<Element>, isOuter: Boolean = true): TypeName {
    if(classElements.any{ (it.asType().asTypeName().toString() == type.asTypeName().toString()) })
        return ClassName("uk.gibby.redis.generated", "${type.asTypeName().toString().split(".").last()}Result")
    if(type.isEnum()) return SerializableResult::class.asClassName().parameterizedBy(type.asTypeName())
    when (type.asTypeName()) {
        String::class.asClassName(), javaString -> return StringResult::class.asClassName()
        Double::class.asClassName() -> return DoubleResult::class.asClassName()
        Long::class.asClassName(), javaLong -> return LongResult::class.asClassName()
        Boolean::class.asClassName(), javaBool -> return BooleanResult::class.asClassName()
        else -> {}
    }
    return when{
        "^java\\.util\\.List<.*>$".toRegex().matches(type.asTypeName().toString()) -> {
            val innerType = (type as DeclaredType).typeArguments[0]
            val innerResult = getDefaultType(innerType, classElements)
            println(innerType.asTypeName())
            ArrayResult::class.asClassName()
                .parameterizedBy(toKotlin(innerType), innerResult)
        }
        else -> throw Exception("Type should be primitive, array or annotated with @RedisType. Found: '${type.asTypeName()}'")
    }
}
fun toKotlin(type: TypeMirror): TypeName {
    var typeIter = type
    var levels = 0
    while (typeIter.asTypeName().toString().matches("^java\\.util\\.List<.*>$".toRegex())){
        typeIter = (typeIter as DeclaredType).typeArguments[0]
        levels++
    }
    val bottomClass = when(typeIter.asTypeName()){
        javaLong -> Long::class.asClassName()
        javaString -> String::class.asClassName()
        javaBool -> Boolean::class.asClassName()

        else -> typeIter.asTypeName()
    }
    var name = bottomClass
    for(i in 1..levels){
        name = List::class.asClassName().parameterizedBy(name)
    }
    return name
}