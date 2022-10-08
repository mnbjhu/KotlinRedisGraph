import uk.gibby.redis.core.RedisGraph

import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import schemas.MyNumber
import uk.gibby.redis.functions.math.avg
import uk.gibby.redis.functions.math.max
import uk.gibby.redis.functions.math.min
import uk.gibby.redis.functions.math.sum
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match

/**
 * Functions test
 *
 * @constructor Create empty Functions test
 */
class FunctionsTest {
    private val numbersGraph = RedisGraph(
        name = "numbers",
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
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)) { attr, iter ->
            attr[num] = iter
        }
    }
    /**
     * Delete all
     *
     */
    private fun deleteAll() = numbersGraph.query {
        val number = match(MyNumber())
        delete(number)
    }

    /**
     * Test averages
     *
     */
    @Test
    fun `Test Averages`(){
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){ attr, iter -> attr[num] = iter }
        numbersGraph.query {
            val number = match(MyNumber())
            avg(number.num)
        }.first() `should be equal to` 8.0
    }

    /**
     * Test max
     *
     */
    @Test
    fun `Test Max`(){
        numbersGraph.query {
            val number = match(MyNumber())
            max(number.num)
        }.first() `should be equal to` 13.0
    }

    /**
     * Test min
     *
     */
    @Test
    fun `Test Min`(){
        numbersGraph.query {
            val number = match(MyNumber())
            min(number.num)
        }.first() `should be equal to` 1.0
    }

    /**
     * Test sum
     *
     */
    @Test
    fun `Test Sum`(){
        numbersGraph.query {
            val number = match(MyNumber())
            sum(number.num)
        }.first() `should be equal to` 24.0
    }

}