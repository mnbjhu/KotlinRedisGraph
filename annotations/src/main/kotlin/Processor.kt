import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import uk.gibby.redis.annotation.Node
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.annotation.Relates
import java.io.File
import javax.annotation.processing.*
import javax.annotation.processing.Processor
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException


@AutoService(Processor::class)
@SupportedOptions("kapt.kotlin.generated")
class Processor: AbstractProcessor() {
    private val targetDirectory: String
        get() = processingEnv.options["kapt.kotlin.generated"]
            ?: throw IllegalStateException("Unable to get target directory")
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()
    override fun getSupportedAnnotationTypes() = mutableSetOf(
        Node::class.java.canonicalName,
        RedisType::class.java.canonicalName
    )
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) return false
        if (roundEnv.processingOver()) return true
        val classElements = roundEnv.getElementsAnnotatedWith(RedisType::class.java).filter { it.kind.isClass }

        classElements.forEach { processRedisTypeClass(it!!, classElements).writeTo(File(targetDirectory)) }
        val nodeElements = roundEnv.getElementsAnnotatedWith(Node::class.java).filter { it.kind.isClass }
        nodeElements.forEach { node ->
            val relationships = node.getAnnotationsByType(Relates::class.java)
                .associateWith { relates ->
                    classElements.find {
                        val typeName = try { relates.data.asTypeName() } catch (e: MirroredTypeException){ e.typeMirror.asTypeName() }
                        it.asType().asTypeName().toString() == typeName.toString()
                    }!!
                }
                .map {(relation, relationType) ->
                    val spec = processRedisRelation(
                        relation.by,
                        relationType,
                        classElements,
                        node.asType().asTypeName(),
                        try { relation.to.asTypeName() }catch (e: MirroredTypeException){ e.typeMirror.asTypeName() }
                    )
                    spec.writeTo(File(targetDirectory))
                    Triple(relation, ClassName("uk.gibby.redis.generated", relation.by.capitalizeFirst() + "Relation"), node.asType().asTypeName())
                }
            processRedisNode(node!!, classElements, relationships).writeTo(File(targetDirectory))
        }
        return true
    }

}
