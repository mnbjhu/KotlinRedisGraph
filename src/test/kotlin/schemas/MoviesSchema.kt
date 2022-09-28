package schemas

import uk.gibby.redis.core.*

class Actor: UnitNode(){
    val name by string()
    val actorId by long()
    val actedIn = relates(ActedIn::class)
}
class Movie: UnitNode(){
    val title by string()
    val releaseYear by long()
    val movieId by long()
}
class ActedIn: RedisRelation<Actor, Movie>(){
    val role by string()
}
