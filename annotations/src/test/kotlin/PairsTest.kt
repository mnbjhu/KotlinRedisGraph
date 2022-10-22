import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.ResultParent.Companion.long
import uk.gibby.redis.results.literal
import uk.gibby.redis.results.of
import uk.gibby.redis.results.pair
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

class PairsTest{
    private val graph = RedisGraph(
        name = "pairs",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Test
    fun `Basic Pairs Test`(){
        graph.query {
            val myPair = pair(::long, ::long) of (1L to 2L)
            myPair
        }.first() `should be equal to` (1L to 2L)
    }
}