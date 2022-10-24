import org.amshove.kluent.`should be equal to`
import org.junit.Test
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.generated.ArrayRedisStructAttribute
import uk.gibby.redis.generated.ListStructAttribute
import uk.gibby.redis.generated.Vector3Attribute
import uk.gibby.redis.statements.Match.Companion.match

enum class MyEnum{
    GoodEnum,
    BadEnum
}

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

@RedisType
data class ArrayRedisStruct(val data: List<List<Vector3>>)

@RedisType
data class DoubleTest(val myDouble: Double)

@RedisType
data class StringTest(val data: String)

@RedisType
data class BooleanTest(val data: Boolean)
@RedisType
data class StringArrayTest(val data: List<String>)

@RedisType
data class BooleanArrayTest(val data: List<Boolean>)

// @RedisType
data class EnumTest(val data: MyEnum)




class VectorNode: UnitNode() {
   val vector by Vector3Attribute()
}

class ListNode: UnitNode() {
   val struct by ListStructAttribute()
}

class ComplexNode: UnitNode(){
    val data by ArrayRedisStructAttribute()
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
        graph.create(::VectorNode){
            it[vector] = Vector3(1.0, 2.0, 3.0)
        }
        graph.query {
            val node = match(::VectorNode)
            node.vector
        }.first() `should be equal to` Vector3(1.0, 2.0, 3.0)
    }
    @Test
    fun testList(){
        graph.create(::ListNode){
            it[struct] = ListStruct(listOf(-1L, 0L, 1L))
        }
        graph.query {
            val node = match(::ListNode)
            node.struct
        }.first() `should be equal to` ListStruct(listOf(-1L, 0L, 1L))
    }
    @Test
    fun complexTest(){
        graph.create(::ComplexNode){
            it[data] = ArrayRedisStruct(
                listOf(
                    listOf(Vector3(1.2, 3.4, 5.6), Vector3(1.0, 2.0, 4.0)),
                    listOf(),
                    listOf(Vector3(9.9, 9.9, 9.9))
                )
            )
        }
        graph.query {
            val node = match(::ComplexNode)
            node.data
        }.first() `should be equal to` ArrayRedisStruct(
            listOf(
                listOf(Vector3(1.2, 3.4, 5.6), Vector3(1.0, 2.0, 4.0)),
                listOf(),
                listOf(Vector3(9.9, 9.9, 9.9))
            )
        )
    }
}
