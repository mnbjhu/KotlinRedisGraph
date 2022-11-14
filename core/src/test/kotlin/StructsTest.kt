import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import uk.gibby.redis.attributes.Attribute
import uk.gibby.redis.conditions.equality.eq
import uk.gibby.redis.core.*
import uk.gibby.redis.functions.math.plus
import uk.gibby.redis.results.ResultScope
import uk.gibby.redis.results.StructResult
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.core.ResultParent.Companion.long
import kotlin.reflect.KFunction0

class StructsTest {
    private val structsGraph = RedisGraph("structs",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Before
    fun deleteAll(){
        structsGraph.query { delete(match(StructNode())) }
    }
    @Test
    fun `Basic Test`(){
        structsGraph.create(StructNode::class){
            it[myVector] = Vector2(1, 2)
            it[myLine] = Vector2(1, 2) to Vector2(2, 4)
        }
        structsGraph.query {
            val node = match(StructNode())
            node.myVector
        }.first() `should be equal to` Vector2(1, 2)
        structsGraph.query {
            val node = match(StructNode())
            node.myVector.x
        }.first() `should be equal to` 1L
        structsGraph.query {
            val v = match(StructNode())
            v.myVector eq Vector2(1, 2)
        }.first() `should be equal to` true
        structsGraph.query {
            val v = match(StructNode())
           v.myVector + v.myVector
        }.first() `should be equal to` Vector2(2, 4)
        structsGraph.query {
            val v = match(StructNode())
            v.myLine
        }.first() `should be equal to` (Vector2(1, 2) to Vector2(2, 4))
        structsGraph.query {
            val v = match(StructNode())
            v.myLine.a
        }.first() `should be equal to` Vector2(1, 2)
        structsGraph.query {
            val v = match(StructNode())
            v.myLine.a.x
        }.first() `should be equal to` 1L
    }
}
operator fun <U: StructResult<*>>KFunction0<U>.invoke(builder: U.() -> Unit): U = invoke().apply { builder() }


data class Vector2(val x: Long, val y: Long)

class Vector2Attribute: Vector2Result(), Attribute<Vector2>
open class Vector2Result: StructResult<Vector2>() {
    open val x by long()
    open val y by long()
    override fun ResultScope.getResult() = Vector2(x.result(), y.result())
    override fun ParamMap.setResult(value: Vector2) {
        x[value.x]
        y[value.y]
    }
}
operator fun Vector2Result.plus(other: Vector2Result) = object: Vector2Result(){
    override val x = this@plus.x + other.x
    override val y = this@plus.y + other.y
}
open class LineResult: StructResult<Line>(){
    val a by Vector2Result()
    val b by Vector2Result()
    override fun ResultScope.getResult(): Line = a.result() to b.result()

    override fun ParamMap.setResult(value: Line) {
        a[value.first]
        b[value.second]
    }
}
typealias Line = Pair<Vector2, Vector2>

class LineAttribute: LineResult(), Attribute<Line>
class StructNode: UnitNode(){
    val myVector by Vector2Attribute()
    val myLine by LineAttribute()
}