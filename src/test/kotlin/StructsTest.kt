import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import uk.gibby.redis.attributes.StructAttribute
import uk.gibby.redis.conditions.equality.ResultEquality.Companion.eq
import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.ResultScope
import uk.gibby.redis.statements.Match.Companion.match

class StructsTest {
    private val structsGraph = RedisGraph("structs",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Test
    fun `Basic Test`(){
        structsGraph.create(StructNode::class){
            it[myStruct] = Vector2(1, 2)
        }
        structsGraph.query {
            val node = match(StructNode())
            node.myStruct
        }.first() `should be equal to` Vector2(1, 2)
        structsGraph.query {
            val node = match(StructNode())
            node.myStruct.x
        }.first() `should be equal to` 1L
        structsGraph.query {
            val v = match(StructNode())
            v.myStruct eq Vector2(1, 2)
        }.first() `should be equal to` true
    }
}
data class Vector2(val x: Long, val y: Long)

class Vector2Attribute: StructAttribute<Vector2>() {
    val x by long()
    val y by long()
    override fun ResultScope.getResult() = Vector2(!x, !y)
    override fun ParamMap.setResult(value: Vector2) {
        x[value.x]
        y[value.y]
    }
    //infix fun eq(other: Vector2Attribute) = (x eq other.x) and (y eq other.y)
}
class StructNode: RedisNode(){
    val myStruct by Vector2Attribute()
}