import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import schemas.NodeTestData
import schemas.ReturningNode
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

class NodeReturns {
    private val graph = RedisGraph(
        name = "nodeGraph",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Before
    fun setup(){
        deleteAll()
        setupList()
    }
    private fun setupList() {
        graph.create(ReturningNode::class) {
            it[data] = "Hello, World!"
            it[number] = 10
        }
    }

    private fun deleteAll() = graph.query {
        val node = match(ReturningNode())
        delete(node)
    }

    @Test
    fun `Test node returns`(){
        graph.query {
            val node = match(ReturningNode())
            node
        }.first() `should be equal to` NodeTestData("Hello, World!", 10)
    }

}