import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.functions.relation.endNode
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test
import uk.gibby.redis.paths.minus
import uk.gibby.redis.results.result
import schemas.Actor
import schemas.Movie
import uk.gibby.redis.conditions.equality.ResultEquality.Companion.eq
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.OrderBy.Companion.orderBy
import uk.gibby.redis.statements.Where.Companion.where
import uk.gibby.redis.statements.Match.Companion.match


class MoviesTest {
    private val moviesGraph = RedisGraph(
        name = "movies",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )

    //@BeforeEach
    private fun deleteAll() {
        moviesGraph.query {
            val (movie, actor) = match(Movie(), Actor())
            delete(movie, actor)
        }
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
        moviesGraph.create(Actor::class, actors) { attr, iter ->
            attr[name] = iter
            attr[actorId] = index++
        }
        moviesGraph.create(Movie::class) {
            it[title] = "Star Wars: Episode V - The Empire Strikes Back"
            it[releaseYear] = 1980
            it[movieId] = 1
        }

        moviesGraph.query {
            val (actor, movie) = match(Actor(), Movie())
            where((actor.actorId eq 1) and (movie.movieId eq 1))
            create(actor - { actedIn { it[role] = "Luke Skywalker" } } - movie)
        }
        moviesGraph.query {
            val (actor, movie) = match(Actor(), Movie())
            where((actor.actorId eq 2) and (movie.movieId eq 1))
            create(actor - { actedIn { it[role] = "Han Solo" } } - movie)
        }
        moviesGraph.query {
            val (actor, movie) = match(Actor(), Movie())
            where((actor.actorId eq 3) and (movie.movieId eq 1))
            create(actor - { actedIn { it[role] = "Princess Leia" } } - movie)
        }
        val moviesTitles = moviesGraph.query {
            val movie = match(Movie())
            movie.title
        }
        moviesTitles `should contain` "Star Wars: Episode V - The Empire Strikes Back"

        val result = moviesGraph.query {
            val movie = match(Movie())
            result(movie.title, movie.releaseYear, movie.movieId)
        }
        val (title, releaseYear, id) = result.first()

        title as String `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"
        releaseYear as Long `should be equal to` 1980
        id as Long `should be equal to` 1

        val actedInMovies = moviesGraph.query {
            val (actor, relation, movie) = match(Actor() - { +actedIn } - Movie())
            where(endNode(relation).movieId eq 1)
            orderBy(actor.actorId)
            result(actor.name, movie.title)
        }

        actedInMovies.size `should be equal to` 3

        val (actorName, movieName) = actedInMovies.last()

        actorName `should be equal to` "Carrie Fisher"
        movieName `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"

        val removedActor = moviesGraph.query {
            val (actor, relationship) = match(Actor() - { actedIn } - Movie())
            where(actor.actorId eq 1)
            delete(relationship)
            actor.name
        }
        removedActor.size `should be equal to` 1
        removedActor.first() `should be equal to` "Mark Hamill"
    }
}
