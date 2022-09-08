import core.RedisGraph
import functions.relation.endNode
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test
import paths.minus
import schemas.Actor
import schemas.Movie

class MoviesTest {
    private val moviesGraph = RedisGraph(
        name = "movies",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    //@BeforeEach
    private fun deleteAll(){
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
        moviesGraph.create(Actor::class, actors) {
            name[it]
            actorId[index++]
        }
        moviesGraph.create(Movie::class) {
            title["Star Wars: Episode V - The Empire Strikes Back"]
            releaseYear[1980]
            movieId[1]
        }

        moviesGraph.queryWithoutResult {
            val (actor, movie) = match(Actor(), Movie())
            where ( (actor.actorId eq 1) and (movie.movieId eq 1) )
            create(actor - { actedIn { role["Luke Skywalker"] } } - movie)
        }
        moviesGraph.queryWithoutResult {
            val (actor, movie) = match(Actor(), Movie())
            where ( (actor.actorId eq 2) and (movie.movieId eq 1) )
            create(actor - { actedIn{ role["Han Solo"] } } - movie)
        }
        moviesGraph.queryWithoutResult {
            val (actor, movie) = match(Actor(), Movie())
            where ( (actor.actorId eq 3) and (movie.movieId eq 1) )
            create( actor - { actedIn{ role["Princess Leia"] } } - movie )
        }
        val movies = moviesGraph.query{
            val movie = match(Movie())
            movie.title
        }
        movies `should contain` "Star Wars: Episode V - The Empire Strikes Back"

        val (title, releaseYear, id) = moviesGraph.query {
            val movie = match( Movie())
            result(movie.title, movie.releaseYear, movie.movieId)
        }.first()

        title as String `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"
        releaseYear as Long `should be equal to` 1980
        id as Long `should be equal to` 1

        val actedInMovies = moviesGraph.query {
            val (actor, relation, movie) = match(Actor() - { +actedIn }  - Movie())
            where (endNode(relation).movieId eq 1)
            orderBy(actor.actorId)
            result(actor.name, movie.title)
        }

        actedInMovies.size `should be equal to` 3

        val (actorName, movieName) = actedInMovies.last()

        actorName `should be equal to` "Carrie Fisher"
        movieName `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"

        val removedActor = moviesGraph.query {
            val (actor, relationship) = match(Actor() - { actedIn }  - Movie())
            where ( actor.actorId eq 1 )
            delete(relationship)
            result(actor.name)
        }
        removedActor.size `should be equal to` 1
        removedActor.first() `should be equal to` "Mark Hamill"
    }
}
