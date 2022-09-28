import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be in`
import uk.gibby.redis.core.RedisGraph

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import schemas.MyNumber
import schemas.NodeWithResult
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

/**
 * Functions test
 *
 * @constructor Create empty Functions test
 */
class NodeResultTest {
    private val nodeGraph = RedisGraph(
        name = "node_results",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @BeforeEach
    fun setup(){
        deleteAll()
        setupData()
    }
    private fun deleteAll(){
        nodeGraph.query {
            val node = match(NodeWithResult())
            delete(node)
        }
    }
    private fun setupData() {
        nodeGraph.create(NodeWithResult::class, listOf(1.0, 10.0, 13.0)) { attr, iter ->
            attr[number] = 10L
        }
    }
    @Test
    fun `Test node result`(){
        nodeGraph.query {
            val node = match(NodeWithResult())
            node
        }.first() `should be equal to` 10L
    }

}