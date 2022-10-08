import com.google.auto.service.AutoService
import uk.gibby.annotation.Node
import uk.gibby.annotation.Relates
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedOptions("kapt.kotlin.generated")
class NodeProcessor: AbstractProcessor() {
    private val targetDirectory: String
        get() = processingEnv.options["kapt.kotlin.generated"]
            ?: throw IllegalStateException("Unable to get target directory")
    override fun getSupportedSourceVersion(): SourceVersion =
        SourceVersion.latestSupported()
    override fun getSupportedAnnotationTypes() =
        mutableSetOf(Node::class.java.canonicalName)
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) {
            return false
        }

        // NOTE: generate only once, subsequent runs provide empty set
        if (roundEnv.processingOver()) {
            return true
        }

        val operations = roundEnv
            .getElementsAnnotatedWith(Node::class.java)
            .filter { it.kind.isClass }
            .map { node ->
                val members = node.enclosedElements
                    .filter { it.kind.isField }
                    .map { it.simpleName to it.kind.declaringClass.declaredClasses.first().kotlin }
                node.getAnnotationsByType(Relates::class.java)
            }
        return true
    }

}