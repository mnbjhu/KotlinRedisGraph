import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.ResultParent.Companion.long
import uk.gibby.redis.results.literal
import uk.gibby.redis.results.of
import uk.gibby.redis.results.pair
import uk.gibby.redis.results.pairAttribute
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

class PairsTest{
    class PairNode: UnitNode(){
        val myPair by pairAttribute(::string, ::string)
    }
    private val graph = RedisGraph(
        name = "pairs",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Test
    fun `Basic Pairs Test`(){
        graph.query { pair(::long, ::long) of (1L to 2L) }.also{
            it.first() `should be equal to` (1L to 2L)
            it.size `should be equal to` 1
        }
    }
    @Test
    fun `Pairs First Element Test`(){
        graph.query {
            val myPair = pair(::long, ::long) of (1L to 2L)
            myPair.first
        }.also{
            it.first() `should be equal to` 1L
            it.size `should be equal to` 1
        }
    }
    @Test
    fun `Pairs Second Element Test`(){
        graph.query {
            val myPair = pair(::long, ::long) of (1L to 2L)
            myPair.second
        }.also{
            it.first() `should be equal to` 2L
            it.size `should be equal to` 1
        }
    }

    @Test
    fun `Pair Attribute Test`(){
        graph.create(::PairNode){ it[myPair] = "first" to "second" }
        graph.query { match(::PairNode).myPair }.first() `should be equal to` ("first" to "second")
    }
}