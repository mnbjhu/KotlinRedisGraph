import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import uk.gibby.redis.core.RedisRelation
import javax.lang.model.element.Element

fun processRedisRelation(
    name: String,
    classElement: Element,
    classElements: List<Element>,
    firstNode: TypeName,
    secondNode: TypeName
): FileSpec {
    val uppercaseName = name.capitalizeFirst()
    val firstClassName = ClassName("uk.gibby.redis.generated", "${firstNode}Node")
    val secondClassName = ClassName("uk.gibby.redis.generated", "${secondNode}Node")
    val superClassName = RedisRelation::class.asTypeName().parameterizedBy(classElement.asType().asTypeName(), firstClassName, secondClassName)
    val nodeClassName = ClassName("uk.gibby.redis.generated", "${uppercaseName}Relation")
    val resultClass = buildRelationClass(nodeClassName, superClassName, classElement, classElements)
    return FileSpec.builder("uk.gibby.redis.generated", "${uppercaseName}Relation")
        .addType(resultClass)
        .build()
}
fun String.capitalizeFirst() = first().uppercase() + drop(1)
private fun buildRelationClass(
    relationClassName: ClassName,
    relationType: ParameterizedTypeName,
    clazz: Element,
    classElements: List<Element>
): TypeSpec {
    val nodeClassBuilder = TypeSpec.classBuilder(relationClassName)
        .superclass(relationType)
    val members = clazz.enclosedElements.filter { it.kind.isField }
    members.forEach { nodeClassBuilder.addProperty(buildNodeProperty(it, classElements)) }
    nodeClassBuilder.addFunction(buildNodeGetResult(clazz.asType().asTypeName(), members))
    return nodeClassBuilder.build()
}