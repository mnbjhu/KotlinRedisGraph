import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldContainSame
import org.junit.Test
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.ResultParent.Companion.string
import uk.gibby.redis.results.*

class MapTest {
    private val graph = RedisGraph(
        "mapTest",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    private val fooBarMap = mapOf(
        "Foo1" to "Bar1",
        "Foo2" to "Bar2",
        "Foo3" to "Bar3",
    )
    @Test
    fun `Basic hash map test`(){
        graph.query {
            map(::string) of fooBarMap
        }.first() shouldContainSame fooBarMap
    }

    @Test
    fun `Get Value Test`(){
        graph.query {
            val myMap = map(::string) of fooBarMap
            myMap["Foo2".literal()]
        }.first() `should be equal to` "Bar2"
    }

    @Test
    fun `Get Keys Test`(){
        graph.query {
            val myMap = map(::string) of fooBarMap
            myMap.keys()
        }.first() shouldContainSame fooBarMap.keys
    }
}