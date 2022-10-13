import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import uk.gibby.annotation.RedisType
import uk.gibby.redis.results.*

import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import kotlin.reflect.*


fun processRedisTypeClass(
    classElement: Element,
    classElements: List<Element>
): FileSpec {
    val superClassName = StructResult::class.asTypeName().parameterizedBy(classElement.asType().asTypeName())
    val resultClassName = ClassName("uk.gibby.redis.generated", "${classElement.simpleName}Result")
    val resultClass = buildResultClass(resultClassName, superClassName, classElement, classElements)
    val attributeClass = buildAttribute(classElement, resultClassName)
    return FileSpec.builder("uk.gibby.redis.generated", "${classElement.simpleName}Type")
        .addType(resultClass)
        .addType(attributeClass)
        .build()
}

private fun buildAttribute(
    classElement: Element,
    resultClassName: ClassName
): TypeSpec {
    val attributesTypeName = Attribute::class
            .asTypeName()
            .parameterizedBy(classElement.asType().asTypeName())
    return TypeSpec
        .classBuilder("${classElement.simpleName}Attribute")
        .superclass(resultClassName)
        .addSuperinterface(attributesTypeName)
        .build()
}

private fun buildResultClass(
    resultClassName: ClassName,
    resultType: ParameterizedTypeName,
    clazz: Element,
    classElements: List<Element>
): TypeSpec {
    val resultClassBuilder = TypeSpec.classBuilder(resultClassName)
        .superclass(resultType)
        .addModifiers(KModifier.OPEN)
    val members = clazz.enclosedElements.filter { it.kind.isField }
    members.forEach { resultClassBuilder.addProperty(buildProperty(it, classElements)) }
    resultClassBuilder.addFunction(buildGetResult(clazz.asType().asTypeName(), members))
    resultClassBuilder.addFunction(buildSetResult(clazz.asType().asTypeName(), members))
    return resultClassBuilder.build()
}

val javaString = ClassName("java.lang", "String")
val javaLong = ClassName("java.lang", "Long")


