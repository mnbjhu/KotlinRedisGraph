import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import javax.lang.model.element.Element

fun buildProperty(member: Element, classElements: List<Element>): PropertySpec {
    return PropertySpec
        .builder(member.simpleName.toString().split(".").last(), getType(member, classElements))
        .addModifiers(KModifier.OPEN)
        .delegate(getArrayTypeFunction(member.asType(), classElements))
        .build()
}
