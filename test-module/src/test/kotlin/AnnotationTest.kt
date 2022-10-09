import org.jetbrains.annotations.TestOnly
import uk.gibby.annotation.RedisType

@RedisType
data class Vector3(val x: Double, val y: Double, val z: Double)

class AnnotationTest{

    fun test(){}
}