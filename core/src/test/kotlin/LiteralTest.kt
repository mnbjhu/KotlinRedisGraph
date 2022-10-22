import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.results.*
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

class LiteralTest {
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
    fun `Create int literal`(){
        structsGraph.create(StructNode::class){
            it[myLine] = Vector2(1,2) to Vector2(3, 4)
            it[myVector] = Vector2(0, 0)
        }
        structsGraph.query { 12.literal() }.apply {
            first() `should be equal to` 12
            size `should be equal to` 1
        }
    }
    @Test
    fun `Create string literal`(){
        structsGraph.create(StructNode::class){
            it[myLine] = Vector2(1,2) to Vector2(3, 4)
            it[myVector] = Vector2(0, 0)
        }
        structsGraph.query { "abc".literal() }.first() `should be equal to` "abc"
    }

    @Test
    fun `Create list literal`(){
        structsGraph.query {
            listOf("abc").literal()
        }.first() `should be equal to` listOf("abc")
    }

    @Test
    fun `Create struct literal`(){
        structsGraph.query {
            literalOf(Vector2Result(), Vector2(1,2))
        }.first() `should be equal to` Vector2(1, 2)
    }
    @Test
    fun `Create list of struct literal`(){
        structsGraph.query { literalOf(array(::Vector2Result), listOf(Vector2(1,2))) }.first() `should be equal to` listOf(Vector2(1, 2))
    }
}