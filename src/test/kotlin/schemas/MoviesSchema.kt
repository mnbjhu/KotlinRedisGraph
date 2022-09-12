package schemas

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation

class Actor: RedisNode(){
    val name by +string()
    val actorId by +long()
    val actedIn = relates(ActedIn::class)
}
class Movie: RedisNode(){
    val title by string()
    val releaseYear by long()
    val movieId by long()
}
class ActedIn: RedisRelation<Actor, Movie>(){
    val role by string()
}
