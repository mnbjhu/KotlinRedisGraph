package schemas

import api.RedisNode
import api.RedisRelation

class Actor(override val instanceName: String) : RedisNode("Actor"){
    val name = string("name")
    val actorId = long("actor_id")
    val actedIn = relates(ActedIn::class)
}
class Movie(override val instanceName: String) : RedisNode("Movie"){
    val title = string("title")
    val releaseYear = long("release_year")
    val movieId = long("movie_id")
}
class ActedIn(from: Actor, to: Movie, override val instanceName: String):
    RedisRelation<Actor, Movie>(from, to, "ACTED_IN"){
    val role = string("role")
}
