import org.amshove.kluent.`should be equal to`
import org.junit.Before
import org.junit.Test
import uk.gibby.redis.annotation.Node
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.annotation.Relates
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.invoke
import uk.gibby.redis.generated.UserNode
import uk.gibby.redis.generated.UserSessionNode
import uk.gibby.redis.paths.minus
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.statements.WithAs.Companion.using

class UsersTest {

    @RedisType
    class EmptyData

    @Node
    @Relates(to = User::class, by = "invited", data = EmptyData::class)
    @Relates(to = UserSession::class, by = "authorisedBy", data = EmptyData::class)
    data class User(val username: String, val passwordHash: String)

    @Node
    data class UserSession(val username: String, val key: String)



    private val graph = RedisGraph(
        name = "UsersTest",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Before
    fun deleteAll(){
        graph.query {
            val user = match(::UserNode)
            delete(user)

        }
        graph.query {
            val session = match(::UserSessionNode)
            delete(session)
        }
    }
    @Test
    fun `Test Send Invite When Created`(){
        graph.query {
            val user1 = create(::UserNode{it[username] = "TestUser1"; it[passwordHash] = "###"})
            val user2 = create(::UserNode{it[username] = "TestUser2"; it[passwordHash] = "###"})
            val (user1Ref, user2Ref) = using(user1, user2)
            create(user1Ref - { invited } - user2Ref)
        }
        graph.query {
            val (user) = match(::UserNode{ it[username] = "TestUser1" } - { invited } - ::UserNode{ it[username] = "TestUser2" })
            user.username
        } `should be equal to` listOf("TestUser1")
    }

    @Test
    fun `Test Send Invite`(){
        graph.query {
            create(::UserNode{it[username] = "TestUser1"; it[passwordHash] = "###"})
            create(::UserNode{it[username] = "TestUser2"; it[passwordHash] = "###"})
        }
        graph.query {
            val (user) = match(::UserNode{ it[username] = "TestUser1" } - { invited } - ::UserNode{ it[username] = "TestUser2" })
            user.username
        } `should be equal to` emptyList()
        graph.query {
            val (from, to) = match(
                ::UserNode{ it[username] = "TestUser1" },
                ::UserNode{ it[username] = "TestUser2" }
            )
            create(from - { invited } - to)
        }
    }
    @Test
    fun createUser(){
        val (name: String, pass: String) = "TestUser1" to "###"
        if(graph.query { match(::UserNode{ it[username] = name }) }.isEmpty()){
            graph.query {
                val userNode = create(::UserNode{ it[username] = name; it[passwordHash] = pass })
                val usernameRef = using(userNode.username)
                val (_, session) =
                    create(userNode - { authorisedBy } - ::UserSessionNode { it[username] = usernameRef; it[key] = "123" }).nodes()
                session
            }.first()
        }
    }
}