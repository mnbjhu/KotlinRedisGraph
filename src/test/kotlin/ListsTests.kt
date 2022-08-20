import api.RedisGraph
import conditions.array.Contains.Companion.contains
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import schemas.ListNode


class ListsTests {
    private val listGraph = RedisGraph(
        name = "list",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )

    private fun deleteAll(){
        listGraph.query {
            val myList = match(ListNode())
            delete(myList)
        }
    }
    @Test
    fun `Test Create`(){
        deleteAll()
        listGraph.create(ListNode::class){
            myList[listOf("first", "second", "third")]
        }
    }
    @Test
    fun `Test Get`(){
        deleteAll()
        `Test Create`()
        val result = listGraph.query {
            val myList = match(ListNode())
            result(myList.myList)
        }
        result.size `should be equal to` 1
        val (first, second, third) = result.first()
        first `should be equal to` "first"
        second `should be equal to` "second"
        third `should be equal to` "third"
    }
    @Test
    fun `Test Where`(){
        deleteAll()
        val currentList = mutableListOf(1)
        listGraph.create(ListNode::class, 1..10){
            myList[currentList.map { it.toString() }]
            currentList += currentList.last() + 1
        }
        val lists = listGraph.query {
            val myList = match(ListNode())
            where ( myList.myList contains "5" )
            result(myList.myList)
        }
        lists.size `should be equal to` 6
    }
}