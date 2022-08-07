import api.RedisGraph
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import schemas.ListNode

class ListsTests {
    private val listGraph = RedisGraph(
        name = "list",
        host = "raspberrypi.local",
    )
    @BeforeEach
    fun deleteAll(){
        listGraph.query {
            val listNode = variableOf<ListNode>("listNode")
            delete(listNode)
            result(listNode.myList)
        }
    }
    @Test
    fun `Test Create`(){
        listGraph.create(ListNode::class){
            myList[listOf("first", "second", "third")]
        }
    }
    @Test
    fun `Test Get`(){
        `Test Create`()
        val result = listGraph.query {
            val myList = variableOf<ListNode>("my_list")
            result(myList.myList)
        }
        result.size `should be equal to` 1
        val (first, second, third) = result.first()
        first `should be equal to` "first"
        second `should be equal to` "second"
        third `should be equal to` "third"
    }
    @Disabled
    @Test
    fun `Test Where`(){
        val currentList = mutableListOf(1)
        listGraph.create(ListNode::class, 1..10){
            myList[currentList.map{ it.toString() }]
            //currentList += currentList.first() +1
        }
    }
}