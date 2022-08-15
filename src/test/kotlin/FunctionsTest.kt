import api.RedisGraph
import functions.math.Average
import functions.math.Max
import functions.math.Min
import functions.math.Sum
import org.amshove.kluent.`should be equal to`
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
        host = "raspberrypi.local",
    )

    /**
     * Delete all
     *
     */
    fun deleteAll() = numbersGraph.query {
        val number = nodeOf<MyNumber>("number")
        delete(number)
    }

    /**
     * Test averages
     *
     */
    @Test
    fun `Test Averages`(){

        deleteAll()
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){
            num[it]
        }
        numbersGraph.query {
            val number = nodeOf<MyNumber>("number")
            result(Average(number.num))
        }.first() `should be equal to` 8.0
    }

    /**
     * Test max
     *
     */
    @Test
    fun `Test Max`(){
        deleteAll()
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){
            num[it]
        }
        numbersGraph.query {
            val number = nodeOf<MyNumber>("number")
            result(Max(number.num))
        }.first() `should be equal to` 13.0
    }

    /**
     * Test min
     *
     */
    @Test
    fun `Test Min`(){
        deleteAll()
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){
            num[it]
        }
        numbersGraph.query {
            val number = nodeOf<MyNumber>("number")
            result(Min(number.num))
        }.first() `should be equal to` 1.0
    }

    /**
     * Test sum
     *
     */
    @Test
    fun `Test Sum`(){
        deleteAll()
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){
            num[it]
        }
        numbersGraph.query {
            val number = nodeOf<MyNumber>("number")
            result(Sum(number.num))
        }.first() `should be equal to` 24.0
    }

}