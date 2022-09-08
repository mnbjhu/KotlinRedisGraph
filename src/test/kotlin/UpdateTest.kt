import core.RedisGraph
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import paths.minus
import schemas.Actor
import schemas.Movie

class UpdateTest {
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

        moviesGraph.query {
            val (actor, movie) = match(Actor(), Movie())
            where ( (actor.actorId eq 1) and (movie.movieId eq 1) )
            create(actor - { actedIn { role["Luke Skywalker"] } } - movie)
        }
        moviesGraph.query {
            val (actor, movie) = match(Actor(), Movie())
            where ( (actor.actorId eq 2) and (movie.movieId eq 1) )
            create(actor - { actedIn{ role["Han Solo"] } } - movie)
        }
        moviesGraph.query {
            val (actor, movie) = match(Actor(), Movie())
            where ( (actor.actorId eq 3) and (movie.movieId eq 1) )
            create( actor - { actedIn{ role["Princess Leia"] } } - movie )
        }

        moviesGraph.query{
            val movie = match(Movie())
            set { movie.title setTo  "asdasd" }
            result(movie.title)
        }.first() `should be equal to` "asdasd"
    }
}
