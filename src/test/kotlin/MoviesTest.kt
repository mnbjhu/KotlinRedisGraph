import api.RedisGraph
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test
import schemas.Actor
import schemas.Movie

class MoviesTest {
    @Test
    fun `Movie Examples`() {
        /**
         * First, let's delete the movies graph (if exists).
         * Note that the entire graph data is accessible using a single key.
         */
        val graph = RedisGraph("movies", "raspberrypi.local")
        graph.client.graphDelete("movies")
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
        graph.create(Actor::class, actors) {
            name[it]
            actorId[index++]
        }
        graph.create(Movie::class) {
            title["Star Wars: Episode V - The Empire Strikes Back"]
            releaseYear[1980]
            movieId[1]
        }

        graph.query {
            val actor = variableOf<Actor>("actor")
            val movie = variableOf<Movie>("movie")
            where {
                (actor.actorId eq 1) and (movie.movieId eq 1)
            }
            create {
                /**
                 * Now we can create 'ACTED_IN' relationships between actors and the movies.
                 * Mark Hamill played Luke Skywalker in Star Wars: Episode V - The Empire Strikes Back'
                 */
                val actedIn = actor.actedIn("r") { role["Luke Skywalker"] } - movie
                result(actedIn.role) { actedIn.role() }
            }
        }
        graph.query {
            val actor = variableOf<Actor>("actor")
            val movie = variableOf<Movie>("movie")
            where {
                (actor.actorId eq 2) and (movie.movieId eq 1)
            }
            create {
                val actedIn = actor.actedIn("relationship") { role["Han Solo"] } - movie
                result(actedIn.role) { actedIn.role() }
            }
        }
        graph.query {
            val actor = variableOf<Actor>("actor")
            val movie = variableOf<Movie>("movie")
            where {
                (actor.actorId eq 3) and (movie.movieId eq 1)
            }
            create {
                val actedIn = actor.actedIn("relationship") { role["Princess Leila"] } - movie
                result(actedIn.role) { actedIn.role() }
            }
        }

        val movies = graph.query {
            val movie = variableOf<Movie>("movie")
            result(movie.title){movie.title()}
        }
        movies `should contain` "Star Wars: Episode V - The Empire Strikes Back"

        val (title, releaseYear, id) = graph.query {
            val movie = variableOf<Movie>("movie")
            result(movie.title, movie.releaseYear, movie.movieId){
                Triple(movie.title(), movie.releaseYear(), movie.movieId())
            }
        }.first()

        title `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"
        releaseYear `should be equal to` 1980
        id `should be equal to` 1

        val actedIn = graph.query {
            val actor = variableOf<Actor>("actor")
            val (movie) = actor.actedIn("relationship")
            where {
                movie.movieId eq 1
            }
            result(actor.name, movie.title){
                actor.name() to movie.title()
            }
        }

        actedIn.size `should be equal to` 3

        val (actorName, movieName) = actedIn.last()

        actorName `should be equal to` "Carrie Fisher"
        movieName `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"

        val removedRoles = graph.query {
            val actor = variableOf<Actor>("actor")
            val (_, relationship) = actor.actedIn("movie")
            where { actor.actorId eq 1 }
            delete(relationship)
            result(relationship.role){relationship.role()}
        }
        removedRoles.size `should be equal to` 1
        removedRoles.first() `should be equal to` "Luke Skywalker"
    }
}
