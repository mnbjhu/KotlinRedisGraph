import org.amshove.kluent.`should be equal to`
import org.junit.Test
import uk.gibby.annotation.RedisType
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.generated.ListStructAttribute
import uk.gibby.redis.generated.Vector3Attribute
import uk.gibby.redis.statements.Match.Companion.match

@RedisType
data class Vector3(
    val x: Double,
    val y: Double,
    val z: Double,
)

@RedisType
data class LongStruct(val x: Long)

@RedisType
data class Nested(val struct: Vector3)

@RedisType
data class ListStruct(val data: List<Long>)
@RedisType
data class NestedListStruct(val struct: ListStruct)
/*
@RedisType
data class Array2DStruct(val values: List<List<Long>>)

 */
class VectorNode: RedisNode() {
    val vector by Vector3Attribute()
}
class ListNode: RedisNode() {
    val struct by ListStructAttribute()
}

class AnnotationTest{
    private val graph = RedisGraph(
        name = "annotations",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Test
    fun testStruct(){
        graph.create(VectorNode::class){
            it[vector] = Vector3(1.0, 2.0, 3.0)
        }
        graph.query {
            val node = match(VectorNode())
            node.vector
        }.first() `should be equal to` Vector3(1.0, 2.0, 3.0)
    }
    @Test
    fun testList(){
        graph.create(ListNode::class){
            it[struct] = ListStruct(listOf(-1L, 0L, 1L))
        }
        graph.query {
            val node = match(ListNode())
            node.struct
        }.first() `should be equal to` ListStruct(listOf(-1L, 0L, 1L))
    }
}
