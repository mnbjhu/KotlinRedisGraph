import org.amshove.kluent.`should be equal to`
import org.junit.Test
import schemas.Actor
import schemas.Movie
import uk.gibby.redis.conditions.equality.eq
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.toValue
import uk.gibby.redis.paths.minus
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.statements.Update.Companion.set
import uk.gibby.redis.statements.Where.Companion.where

class UpdateTest {
    private val moviesGraph = RedisGraph(
        name = "movies",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )

    private fun deleteAll() = moviesGraph.query {
        val (movie, actor) = match(Movie(), Actor())
        delete(movie, actor)
    }


    @Test
    fun `Movie Examples`() {
        /**
         * First, let's delete the movies graph (if exists).
         * Note that the entire graph data is accessible using a single key.
         */
        deleteAll()
        /**
         * Let's add three nodes that represent actors and then add a node to represent a movie.
         * Note that the graph data structure 'movies' will be automatically created for us as and the nodes are added to it.
         */
        var index = 1L
        val actors = listOf(
            "Mark Hamill",
            "Harrison Ford",
            "Carrie Fisher"
        )
        moviesGraph.create(::Actor, actors) { attr, iter ->
            attr[name] = iter
            attr[actorId] = index++
        }
        moviesGraph.create(::Movie) {
            it[title] = "Star Wars: Episode V - The Empire Strikes Back"
            it[releaseYear] = 1980
            it[movieId] = 1
        }

        moviesGraph.query {
            val (actor, movie) = match(::Actor, ::Movie)
            where((actor.actorId eq 1) and (movie.movieId eq 1))
            create(actor - { actedIn { it[role] = "Luke Skywalker" } } - movie)
        }
        moviesGraph.query {
            val (actor, movie) = match(::Actor, ::Movie)
            where((actor.actorId eq 2) and (movie.movieId eq 1))
            create(actor - { actedIn { it[role] = "Han Solo" } } - movie)
        }
        moviesGraph.query {
            val (actor, movie) = match(::Actor, ::Movie)
            where((actor.actorId eq 3) and (movie.movieId eq 1))
            create(actor - { actedIn { it[role] = "Princess Leia" } } - movie)
        }
        moviesGraph.query {
            val movie = match(::Movie)
            set(movie.title toValue "New Title")
            movie.title
        }.first() `should be equal to` "New Title"
    }
}
