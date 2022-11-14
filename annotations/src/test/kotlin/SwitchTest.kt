import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import uk.gibby.redis.annotation.Node
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.functions.math.plus
import uk.gibby.redis.functions.switch
import uk.gibby.redis.generated.SwitchTestNode
import uk.gibby.redis.results.primitive.LongResult
import uk.gibby.redis.results.primitive.StringResult
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

@Node
data class SwitchTest(val number: Long)

class MyTest{
    private val graph = RedisGraph(
        name = "Switch",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Before
    fun setup(){
        deleteAll()
        createTestData()
    }
    private fun deleteAll() = graph.query{
        val node = match(::SwitchTestNode)
        delete(node)
    }
    private fun createTestData() = graph.create(::SwitchTestNode) { it[number] = 1L }

    @Test
    fun `Test from literal to literal`() {
        graph.query {
            val node = match(::SwitchTestNode)
            switch(node.number, StringResult::class) {
                1L then "Test"
                2L then "Test2"
            }
        }.first() `should be equal to` "Test"

    }
    @Test
    fun `Test from literal to reference`() {
        graph.query {
            val node = match(::SwitchTestNode)
            switch(node.number, LongResult::class) {
                1L then node.number
                2L then node.number + 1
            }
        }.first() `should be equal to` 1L

    }
    @Test
    fun `Test from reference to literal`() {
        graph.query {
            val node = match(::SwitchTestNode)
            val numberPlusOne = node.number + 1
            switch(node.number, StringResult::class) {
                node.number then "Test"
                numberPlusOne then "Test2"
            }
        }.first() `should be equal to` "Test"

    }
    @Test
    fun `Test from reference to reference`() {
        graph.query {
            val node = match(::SwitchTestNode)
            val numberPlusOne = node.number + 1
            switch(node.number, LongResult::class) {
                node.number then numberPlusOne
                numberPlusOne then node.number
            }
        }.first() `should be equal to` 2L
    }
}