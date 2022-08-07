import api.RedisGraph
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import schemas.ListNode


class ListsTests {
    private val listGraph = RedisGraph(
        name = "list",
        host = "raspberrypi.local",
    )

    fun deleteAll(){
        listGraph.query {
            val listNode = variableOf<ListNode>("listNode")
            delete(listNode)
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
            val myList = variableOf<ListNode>("my_list")
            result(myList.myList){
                myList.myList()
            }
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
            myList[listOf("first", "second", "third")]
            //currentList += currentList.first() +1
        }
    }
}