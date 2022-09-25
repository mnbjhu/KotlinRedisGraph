import uk.gibby.redis.core.RedisGraph
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.junit.jupiter.api.Test
import uk.gibby.redis.statements.Match.Companion.match
import schemas.ListNode
import uk.gibby.redis.conditions.array.contains
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Where.Companion.where
import uk.gibby.redis.functions.string.Contains.Companion.contains


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
            it[myList] = listOf("first", "second", "third")
        }
    }
    @Test
    fun `Test Get`(){
        deleteAll()
        `Test Create`()
        val result = listGraph.query {
            val myList = match(ListNode())
            myList.myList
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
        listGraph.create(ListNode::class, 1..10){attr, _ ->
            attr[myList] = currentList.map { it.toString() }
            currentList += currentList.last() + 1
        }
        val lists = listGraph.query {
            val myList = match(ListNode())
            where ( myList.myList contains "5" )
            myList.myList
        }
        lists.size `should be equal to` 6
    }
    @Test
    fun `Test Unwind`(){
        deleteAll()
        val currentList = mutableListOf(1)
        listGraph.create(ListNode::class, 1..10){attr, _ ->
            attr[myList] = currentList.map { it.toString() }
            currentList += currentList.last() + 1
        }
        val lists = listGraph.query {
            val myList = match(ListNode())
            val element = unwind(myList.myList)
            element
        }
        lists `should contain same` listOf<Long>(
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
        ).map { it.toString() }
    }
    @Test
    fun `Test map`(){
        deleteAll()
        `Test Create`()
        listGraph.query {
            val node = match(ListNode())
            node.myList.map { it contains "r" }
        }.first() `should be equal to` listOf(true, false, true)
    }
    @Test
    fun `Test any`(){
        deleteAll()
        `Test Create`()
        listGraph.query {
            val node = match(ListNode())
            node.myList.any { it contains "r" }
        }.first() `should be equal to` true
    }
    @Test
    fun `Test all`(){
        deleteAll()
        `Test Create`()
        listGraph.query {
            val node = match(ListNode())
            node.myList.all { it contains "r" }
        }.first() `should be equal to` false
    }
    @Test
    fun `Test none`(){
        deleteAll()
        `Test Create`()
        listGraph.query {
            val node = match(ListNode())
            node.myList.none { it contains "r" }
        }.first() `should be equal to` false
    }
    @Test
    fun `Test single Ex 1`(){
        deleteAll()
        `Test Create`()
        listGraph.query {
            val node = match(ListNode())
            node.myList.single { !(it contains "r") }
        }.first() `should be equal to` true
    }
    @Test
    fun `Test single Ex 2`(){
        deleteAll()
        `Test Create`()
        listGraph.query {
            val node = match(ListNode())
            node.myList.single { it contains "r" }
        }.first() `should be equal to` false
    }
}