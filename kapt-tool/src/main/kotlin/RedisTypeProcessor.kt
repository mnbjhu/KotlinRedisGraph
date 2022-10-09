import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import uk.gibby.annotation.RedisType

import uk.gibby.redis.results.StructResult
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import kotlin.reflect.*

fun processType(
    clazz: Element
): FileSpec {
    val resultType = StructResult::class.asTypeName().parameterizedBy(clazz.asType().asTypeName())


    val resultClassBuilder = TypeSpec.classBuilder("${clazz.simpleName}Result")
        .addSuperinterface(resultType)
    clazz.enclosedElements
        .filter { it.kind.isField }
        .forEach { resultClassBuilder.addProperty(getPropertySpec(it)) }
    return FileSpec.builder("uk.gibby.redis.generated", "${clazz.simpleName}Type")
        .addType(resultClassBuilder.build())
        .build()
}
fun getPropertySpec(member: Element) = PropertySpec
    .builder(
        member.simpleName.toString(),
        getType(member.asType().asTypeName()),
        //member.asType().asTypeName()
    )
    .addModifiers(
        KModifier.OPEN,

    )
    .delegate(CodeBlock.builder().addStatement("::%M", getType(member.asType().asTypeName())).build())
    .build()
fun getType(type: TypeName): MemberName = when(type){
    ClassName("java.lang", "String") -> MemberName("uk.gibby.redis.result","StringResult", )
    ClassName("java.lang", "Long") -> MemberName( "uk.gibby.redis.core.ResultParent.Companion","long",)
    ClassName("java.lang", "Double") -> MemberName("uk.gibby.redis.core.ResultParent.Companion","double", )
    ClassName("java.lang", "Boolean") -> MemberName( "uk.gibby.redis.core.ResultParent.Companion","boolean",)
    else -> throw Exception("Invalid property type: '${type}'")
}



@AutoService(Processor::class)
@SupportedOptions("kapt.kotlin.generated")
class RedisTypeProcessor: AbstractProcessor() {
    private val targetDirectory: String
        get() = processingEnv.options["kapt.kotlin.generated"]
            ?: throw IllegalStateException("Unable to get target directory")
    override fun getSupportedSourceVersion(): SourceVersion =
        SourceVersion.latestSupported()
    override fun getSupportedAnnotationTypes() =
        mutableSetOf(RedisType::class.java.canonicalName)
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        //throw Exception("Some exception")
        if (annotations.isEmpty()) {
            return false
        }

        // NOTE: generate only once, subsequent runs provide empty set
        if (roundEnv.processingOver()) {
            return true
        }

        roundEnv
            .getElementsAnnotatedWith(RedisType::class.java)
            .filter { it.kind.isClass }
            .forEach { processType(it!!).writeTo(File(targetDirectory)) }

        return true
    }

}
