import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.results.ResultScope
import javax.lang.model.element.Element

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