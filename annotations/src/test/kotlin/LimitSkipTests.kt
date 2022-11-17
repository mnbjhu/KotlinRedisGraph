import org.amshove.kluent.`should be equal to`
import org.junit.Test
import uk.gibby.redis.annotation.Node
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.invoke
import uk.gibby.redis.generated.OrderedItemNode
import uk.gibby.redis.results.LongResult
import uk.gibby.redis.results.array
import uk.gibby.redis.results.of
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Limit.Companion.limit
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.statements.OrderBy.Companion.orderBy
import uk.gibby.redis.statements.OrderBy.Companion.orderByDesc
import uk.gibby.redis.statements.Skip.Companion.skip

@Node
data class OrderedItem(val order: Long)

class LimitSkipTests {
    private val graph = RedisGraph(
        name = "limit_skip_test",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Test
    fun `Test literal limit`(){
        graph.query {
            val item = unwind(array { LongResult() } of List(10){it.toLong()})
            limit(5)
            item
        }.also {
            it.size `should be equal to` 5
            it `should be equal to` listOf(0, 1, 2, 3, 4)
        }
    }
    @Test
    fun `Test literal skip`(){
        graph.query {
            val item = unwind(array { LongResult() } of List(10){it.toLong()})
            skip(1)
            limit(5)
            item
        }.also {
            it.size `should be equal to` 5
            it `should be equal to` listOf(1, 2, 3, 4, 5)
        }
    }

    @Test
    fun `Test OrderBy Desc`(){
        graph.query {
            val x = unwind(array{ LongResult() } of listOf(1,2,3))
            val node = create(::OrderedItemNode{it[order] = x})
            orderByDesc(node.order)
            node.order
        } `should be equal to` listOf(3, 2, 1)
    }
}