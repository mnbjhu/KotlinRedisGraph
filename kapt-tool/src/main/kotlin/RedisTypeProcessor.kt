import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.toKmClass
import uk.gibby.annotation.RedisType
import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.results.*

import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import kotlin.reflect.*


val notOperator = MemberName("uk.gibby.redis.results.ResultScope", KOperator.NOT)
fun processType(
    clazz: Element
): FileSpec {
    val resultType = StructResult::class.asTypeName().parameterizedBy(clazz.asType().asTypeName())
    val resultClassName = ClassName("uk.gibby.redis.generated", "${clazz.simpleName}Result");
    val resultClassBuilder = TypeSpec.classBuilder(resultClassName)
        .superclass(resultType)
        .addModifiers(KModifier.OPEN)
    val members = clazz.enclosedElements
        .filter { it.kind.isField }
        .filterNotNull()
    members.forEach { resultClassBuilder.addProperty(getPropertySpec(it, elements)) }
    resultClassBuilder.addFunction(buildGetResult(clazz.asType().asTypeName(), members))
    resultClassBuilder.addFunction(buildSetResult(clazz.asType().asTypeName(), members))
    val resultClass = resultClassBuilder.build()
    val attributesTypeName = Attribute::class.asTypeName().parameterizedBy(clazz.asType().asTypeName())
    val attributeClass = TypeSpec
        .classBuilder("${clazz.simpleName}Attribute")
        .superclass(resultClassName)
        .addSuperinterface(attributesTypeName)
        .build()
    return FileSpec.builder("uk.gibby.redis.generated", "${clazz.simpleName}Type")
        .addType(resultClass)
        .addType(attributeClass)
        .build()
}
fun getPropertySpec(member: Element): PropertySpec{
    return PropertySpec
        .builder(
            member.simpleName.toString(),
            getType(member),

        )
        .addModifiers(KModifier.OPEN)
        .delegate(getArrayTypeFunction(member.asType()))
        .build()
}
val primates = listOf(
    String::class,
    Long::class,
    Double::class,
    Boolean::class
).map { it.asTypeName() }

fun buildGetResult(type: TypeName, members: List<Element>) =
    FunSpec.builder("getResult")
        .receiver(ResultScope::class)
        .addModifiers(KModifier.OVERRIDE)
        .addStatement("return %T(${members.joinToString { "!${it.simpleName}" }})", type)
        .returns(type)
        .build()
fun buildSetResult(type: TypeName, members: List<Element>) =
    FunSpec.builder("setResult")
        .receiver(ParamMap::class)
        .addParameter("value", type)
        .addModifiers(KModifier.OVERRIDE)
        .apply {
            members
                .map { it.simpleName }
                .forEach { addStatement("$it[value.$it]") }
        }
        .build()
val javaLong = ClassName("java.lang", "Long")
val javaList = LIST
fun getType(member: Element): TypeName {
    return if(member.getAnnotation(RedisType::class.java) != null) ClassName("uk.gibby.redis.generated", "${member.asType().asTypeName()}Result")
    else getDefaultType(member.asType())
}
fun getDefaultType(type: TypeMirror): TypeName{
    when (type.asTypeName()) {
        String::class.asClassName() -> return StringResult::class.asClassName()
        Double::class.asClassName() -> return DoubleResult::class.asClassName()
        Long::class.asClassName(), javaLong -> return LongResult::class.asClassName()
        Boolean::class.asClassName() -> return BooleanResult::class.asClassName()
        else -> {}
    }
    return when{
        //try{ type.getAnnotation(RedisType::class.java) != null } catch (e:Exception){ false } -> ClassName("uk.gibby.redis.generated", "${type.asTypeName()}Result")
        //"^java.util.List<.*>$".toRegex().matches(type.asTypeName().toString())
        "^java.util.List<.*>$".toRegex().matches(type.asTypeName().toString()) -> {
            val innerType = (type as DeclaredType).typeArguments[0]
            val innerResult = getDefaultType(innerType)
            println(innerType.asTypeName())
            val innerTypeName = when(val it = innerType.asTypeName()){
                javaLong -> Long::class.asClassName()
                else -> it
            }
            ArrayResult::class.asClassName()
                .parameterizedBy(innerTypeName, innerResult)//.also { println(it) }
        }
        else -> throw Exception("Type should be primitive, array or annotated with @RedisType. Found: '${type.asTypeName()}: ${elements}'")
    }
}
fun guessClassName(type: TypeMirror): ClassName {
    val (_, pkg, name) = "^(.*)\\.(\\w*)$".toRegex().find(type.asTypeName().toString())!!.groupValues
    return ClassName(pkg, name)
}
fun getBaseTypeFunction(type: TypeMirror): MemberName {
    when (type.asTypeName()) {
        ClassName("java.lang", "String") -> return MemberName("uk.gibby.redis.core.ResultParent.Companion", "string")
        Long::class.asClassName(), javaLong -> return MemberName("uk.gibby.redis.core.ResultParent.Companion", "long")
        ClassName("kotlin", "Double") -> return MemberName("uk.gibby.redis.core.ResultParent.Companion", "double")
        ClassName("java.lang", "Boolean") -> return MemberName("uk.gibby.redis.core.ResultParent.Companion", "boolean")
        else -> {}
    }
    return when {
        type.getAnnotation(RedisType::class.java) != null -> MemberName(
            "uk.gibby.redis.generated",
            "${type.asTypeName()}Result"
        )
        else -> throw Exception("Type should be primitive, array or annotated with @RedisType. '${type.asTypeName()}'")
    }
}
fun getArrayTypeFunction(startType: TypeMirror): CodeBlock{
    var type = startType
    var levels = 0
    while("^java.util.List<.*>$".toRegex().matches(type.asTypeName().toString())){
        levels++
        val innerType = (type as DeclaredType).typeArguments[0]
        type = innerType
    }
    val builder = CodeBlock.builder()
    for (i in 0 until levels){
        builder.add("%M(", MemberName("uk.gibby.redis.results", "array"))
    }
    if(levels == 0) builder.add("%M()", getBaseTypeFunction(type)) else builder.add("::%M", getBaseTypeFunction(type))
    for (i in 0 until levels){
        builder.add(")")
    }
    return builder.build()
}



@AutoService(Processor::class)
@SupportedOptions("kapt.kotlin.generated")
class RedisTypeProcessor: AbstractProcessor() {
    private val targetDirectory: String
        get() = processingEnv.options["kapt.kotlin.generated"]
            ?: throw IllegalStateException("Unable to get target directory")
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()
    override fun getSupportedAnnotationTypes() = mutableSetOf(RedisType::class.java.canonicalName)
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) return false
        if (roundEnv.processingOver()) return true
        val elements = roundEnv.getElementsAnnotatedWith(RedisType::class.java)
            .filter { it.kind.isClass }
        elements.forEach {
            it.enclosedElements
                .filter { field -> field.kind.isField }
            processType(it!!).writeTo(File(targetDirectory))
        }
        return true
    }

}
