import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import uk.gibby.redis.attributes.StructAttribute
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
    }
}
data class Vector2(val x: Long, val y: Long)

class Vector2Attribute: StructAttribute<Vector2>() {
    val x by long()
    val y by long()
    override fun ResultScope.getResult() = Vector2(!x, !y)
    override fun setResult(value: Vector2, params: ParamMap) {
        params[x] = value.x
        params[y] = value.y
    }
}
class StructNode: RedisNode(){
    val myStruct by Vector2Attribute()
}