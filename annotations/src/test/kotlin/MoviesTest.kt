import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.junit.Before
import org.junit.Test
import uk.gibby.redis.annotation.Node
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.annotation.Relates
import uk.gibby.redis.conditions.equality.eq
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.generated.ActorNode
import uk.gibby.redis.generated.MovieNode
import uk.gibby.redis.paths.minus
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.statements.Where.Companion.where

@RedisType
data class ActedIn(val role: String)

@Node
data class Movie(val title: String, val releaseYear: Long)

@Node
@Relates(to = Movie::class, by = "actedIn", data = ActedIn::class)
data class Actor(val name: String)

class MoviesTest {
    private val graph = RedisGraph("NewMovieGraph",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Before
    fun deleteAll() {
        graph.query {
            val (actor, movie) = match(ActorNode(), MovieNode())
            delete(actor, movie)
        }
    }
    @Test
    fun `Test movies with annotation`(){

        val actors = listOf(
            "Mark Hamill",
            "Harrison Ford",
            "Carrie Fisher"
        )
        graph.create(ActorNode::class, actors) { attr, iter ->
            attr[name] = iter
        }
        graph.create(MovieNode::class){
            it[title] = "Star Wars: Episode V - The Empire Strikes Back"
            it[releaseYear] = 1980
        }
        graph.query {
            val (actor, movie) = match(ActorNode(), MovieNode())
            where(actor.name eq "Mark Hamill")
            create(actor - { actedIn{ it[role] = "Luke Skywalker" } } - movie)
        }
        graph.query {
            val (actor, movie) = match(ActorNode(), MovieNode())
            where(actor.name eq "Harrison Ford")
            create(actor - { actedIn{ it[role] = "Han Solo" } } - movie)
        }
        graph.query {
            val (actor, movie) = match(ActorNode(), MovieNode())
            where(actor.name eq "Carrie Fisher")
            create(actor - { actedIn { it[role] = "Princess Leia" } } - movie)
        }
        graph.query {
            val (actor, movie) = match(ActorNode() - { actedIn } - MovieNode()).nodes()
            where(movie.releaseYear eq 1980)
            actor.name
        } `should contain same` actors
        graph.query {
            val (actor, actedIn, movie) = match(ActorNode() - { actedIn } -MovieNode())
            where((actor.name eq "Harrison Ford") and
                        (movie.title eq "Star Wars: Episode V - The Empire Strikes Back"))
            actedIn
        }.first().role `should be equal to` "Han Solo"
    }
}
