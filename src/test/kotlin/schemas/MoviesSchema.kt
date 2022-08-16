package schemas

import api.RedisNode
import api.RedisRelation

class Actor : RedisNode("Actor"){
    val name = string("name")
    val actorId = long("actor_id")
    val actedIn = relates(ActedIn::class)
}
class Movie: RedisNode("Movie"){
    val title = string("title")
    val releaseYear = long("release_year")
    val movieId = long("movie_id")
}
class ActedIn: RedisRelation<Actor, Movie>("ACTED_IN"){
    val role = string("role")
}
