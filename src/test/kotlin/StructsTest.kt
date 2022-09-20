import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import uk.gibby.redis.results.Attribute
import uk.gibby.redis.conditions.equality.eq
import uk.gibby.redis.core.*
import uk.gibby.redis.results.ResultScope
import uk.gibby.redis.results.StructResult
import uk.gibby.redis.statements.Match.Companion.match
import kotlin.reflect.KFunction0

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
        structsGraph.query {
            val v = match(StructNode())
           ::Vector2Result{ x[3]; y[3] }
        }.first()
    }
}
//operator fun <U: AttributeParent>KFunction0<U>.invoke(builder: U.() -> Unit): U = invoke().apply { builder() }
operator fun <U: StructResult<*>>KFunction0<U>.invoke(builder: U.() -> Unit): U = invoke().apply { builder() }

data class Vector2(val x: Long, val y: Long)

class Vector2Attribute: Vector2Result(), Attribute<Vector2>
open class Vector2Result(): StructResult<Vector2>() {
    val x by long()
    val y by long()
    override fun ResultScope.getResult() = Vector2(!x, !y)
    override fun ParamMap.setResult(value: Vector2) {
        x[value.x]
        y[value.y]
    }

}
class StructNode: RedisNode(){
    val myStruct by Vector2Attribute()
}