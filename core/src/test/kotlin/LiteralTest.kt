import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.results.*
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

class LiteralTest {
    private val graph = RedisGraph(
        "structs",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Before
    fun deleteAll(){ graph.query { delete(match(::StructNode)) } }
    @Test
    fun `Create int literal`(){
        graph.query { 12.literal() }.apply {
            first() `should be equal to` 12
            size `should be equal to` 1
        }
    }
    @Test
    fun `Create string literal`(){ graph.query { "abc".literal() }.first() `should be equal to` "abc" }

    @Test
    fun `Create list literal`(){
        graph.query { listOf("abc").literal() }.first() `should be equal to` listOf("abc")
    }

    @Test
    fun `Create struct literal`(){
        graph.query {
            ::Vector2Result of Vector2(1,2)
        }.first() `should be equal to` Vector2(1, 2)
    }
    @Test
    fun `Create list of struct literal`(){
        graph.query { literalOf(array(::Vector2Result), listOf(Vector2(1,2))) }.first() `should be equal to` listOf(Vector2(1, 2))
    }
}