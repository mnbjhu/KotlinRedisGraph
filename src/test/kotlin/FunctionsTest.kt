import core.RedisGraph
import functions.math.Average
import functions.math.Max
import functions.math.Min
import functions.math.Sum
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import schemas.MyNumber

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
    @BeforeEach
    fun setup(){
        deleteAll()
        setupList()
    }
    private fun setupList() {
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)) {
            num[it]
        }
    }
    /**
     * Delete all
     *
     */
    private fun deleteAll() = numbersGraph.queryWithoutResult {
        val number = match(MyNumber())
        delete(number)
    }

    /**
     * Test averages
     *
     */
    @Test
    fun `Test Averages`(){
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){ num[it] }
        numbersGraph.query {
            val number = match(MyNumber())
            Average(number.num)
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
            Max(number.num)
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
            Min(number.num)
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
            Sum(number.num)
        }.first() `should be equal to` 24.0
    }

}