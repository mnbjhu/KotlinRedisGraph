package schemas

import api.RedisClass
import api.RedisRelation

class Actor(override val instanceName: String) : RedisClass("Actor"){
    val name = string("name")
    val actorId = int("actor_id")
    val actedIn = relates(ActedIn::class)
}
class Movie(override val instanceName: String) : RedisClass("Movie"){
    val title = string("title")
    val releaseYear = int("release_year")
    val movieId = int("movie_id")
}
class ActedIn(from: Actor, to: Movie, override val instanceName: String):
    RedisRelation<Actor, Movie>(from, to, "ACTED_IN"){
    val role = string("role")
}
