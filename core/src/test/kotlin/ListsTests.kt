import uk.gibby.redis.core.RedisGraph
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.amshove.kluent.shouldContainSame
import uk.gibby.redis.statements.Match.Companion.match
import schemas.ListNode
import org.junit.Test
import uk.gibby.redis.conditions.array.contains
import uk.gibby.redis.core.ResultParent.Companion.long
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Where.Companion.where
import uk.gibby.redis.functions.string.Contains.Companion.contains
import uk.gibby.redis.results.array
import uk.gibby.redis.results.of


class ListsTests {
    private val listGraph = RedisGraph(
        name = "list",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )

    private fun deleteAll(){
        listGraph.query {
            val myList = match(::ListNode)
            delete(myList)
        }
    }
    @Test
    fun `Test Create`(){
        deleteAll()
        listGraph.create(::ListNode){
            it[myList] = listOf(1, 2, 3)
        }
    }
    @Test
    fun `Test Get`(){
        deleteAll()
        `Test Create`()
        val result = listGraph.query {
            val myList = match(::ListNode)
            myList.myList
        }
        result.size `should be equal to` 1
        val (first, second, third) = result.first()
        first `should be equal to` 1
        second `should be equal to` 2
        third `should be equal to` 3
    }
    @Test
    fun `Test Where`(){
        deleteAll()
        listGraph.create(::ListNode, 1L..10L){ attr, iter ->
            val list: List<Long> = (1L..iter).toList()
            attr[myList] = array(::long) of (1L..iter).toList()
        }
        val lists = listGraph.query {
            val myList = match(::ListNode)
            where ( myList.myList contains 5 )
            myList.myList
        }
        lists.size `should be equal to` 6
    }
    @Test
    fun `Test Unwind`(){
        deleteAll()
        listGraph.create(::ListNode, 1..10){attr, it ->
            attr[myList] = (1L..it).toList()
        }
        val lists = listGraph.query {
            val myList = match(::ListNode)
            val element = unwind(myList.myList)
            element
        }
        lists shouldContainSame listOf(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            1, 2, 3, 4, 5, 6, 7, 8,
            1, 2, 3, 4, 5, 6, 7,
            1, 2, 3, 4, 5, 6,
            1, 2, 3, 4, 5,
            1, 2, 3, 4,
            1, 2, 3,
            1, 2,
            1
        )
    }
}