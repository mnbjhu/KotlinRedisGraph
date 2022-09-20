import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import uk.gibby.redis.attributes.StructAttribute
import uk.gibby.redis.core.*
import uk.gibby.redis.functions.math.plus
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

open class Vector2Attribute: StructAttribute<Vector2>() {
    open val x by long()
    open val y by long()
    override fun ResultScope.getResult() = Vector2(!x, !y)
    override fun ParamMap.setResult(value: Vector2) {
        x[value.x]
        y[value.y]
    }
    operator fun plus(other: Vector2Attribute) = object: Vector2Attribute(){
        override val x = this@Vector2Attribute.x + other.x
        override val y = this@Vector2Attribute.x + other.x
    }
}
class StructNode: RedisNode(){
    val myStruct by Vector2Attribute()
}