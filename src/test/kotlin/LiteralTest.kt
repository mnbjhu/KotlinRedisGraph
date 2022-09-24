import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.array
import uk.gibby.redis.core.long
import uk.gibby.redis.core.string
import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.results.LongResult
import uk.gibby.redis.results.StringResult
import uk.gibby.redis.results.literalOf
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

class LiteralTest {
    private val structsGraph = RedisGraph("structs",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @BeforeEach
    fun deleteAll(){
        structsGraph.query { delete(match(StructNode())) }
    }
    @Test
    fun `Create int literal`(){
        structsGraph.create(StructNode::class){
            it[myLine] = Vector2(1,2) to Vector2(3, 4)
            it[myVector] = Vector2(0, 0)
        }
        structsGraph.query {
            val node = match(StructNode())
            literalOf(long(), 12)
        }.first() `should be equal to` 12
    }
    @Test
    fun `Create string literal`(){
        structsGraph.create(StructNode::class){
            it[myLine] = Vector2(1,2) to Vector2(3, 4)
            it[myVector] = Vector2(0, 0)
        }
        structsGraph.query {
            val node = match(StructNode())
            literalOf(string(), "abc")
        }.first() `should be equal to` "abc"
    }

    @Test
    fun `Create list literal`(){
        structsGraph.create(StructNode::class){
            it[myLine] = Vector2(1,2) to Vector2(3, 4)
            it[myVector] = Vector2(0, 0)
        }
        structsGraph.query {
            val node = match(StructNode())
            literalOf(array(string()), listOf("abc"))
        }.first() `should be equal to` listOf("abc")
    }

    @Test
    fun `Create struct literal`(){
        structsGraph.create(StructNode::class){
            it[myLine] = Vector2(1,2) to Vector2(3, 4)
            it[myVector] = Vector2(0, 0)
        }
        structsGraph.query {
            val node = match(StructNode())
            literalOf(Vector2Result(), Vector2(1,2))
        }.first() `should be equal to` Vector2(1, 2)
    }
}