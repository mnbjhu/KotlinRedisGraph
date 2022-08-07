import api.RedisGraph
import functions.math.Average
import functions.math.Max
import functions.math.Min
import functions.math.Sum
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import schemas.MyNumber

class FunctionsTest {
    val numbersGraph = RedisGraph(
        name = "numbers",
        host = "raspberrypi.local",
    )
    fun deleteAll() = numbersGraph.query {
        val number = variableOf<MyNumber>("number")
        delete(number)
        result(number.num)
    }
    @Test
    fun `Test Averages`(){

        deleteAll()
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){
            num[it]
        }
        numbersGraph.query {
            val number = variableOf<MyNumber>("number")
            result(Average(number.num))
        }.first() `should be equal to` 8.0
    }
    @Test
    fun `Test Max`(){
        deleteAll()
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){
            num[it]
        }
        numbersGraph.query {
            val number = variableOf<MyNumber>("number")
            result(Max(number.num))
        }.first() `should be equal to` 13.0
    }
    @Test
    fun `Test Min`(){
        deleteAll()
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){
            num[it]
        }
        numbersGraph.query {
            val number = variableOf<MyNumber>("number")
            result(Min(number.num))
        }.first() `should be equal to` 1.0
    }
    @Test
    fun `Test Sum`(){
        deleteAll()
        numbersGraph.create(MyNumber::class, listOf(1.0, 10.0, 13.0)){
            num[it]
        }
        numbersGraph.query {
            val number = variableOf<MyNumber>("number")
            result(Sum(number.num))
        }.first() `should be equal to` 24.0
    }

}