package schemas

import UnitNode
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.core.UnitRelation


class Actor: UnitNode(){
    val name by string()
    val actorId by long()
    val actedIn = relates(ActedInRelation::class)
}
class Movie: UnitNode(){
    val title by string()
    val releaseYear by long()
    val movieId by long()
}
class ActedInRelation: UnitRelation<Actor, Movie>(){
    val role by string()
}
