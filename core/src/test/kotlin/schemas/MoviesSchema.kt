package schemas

import uk.gibby.annotation.Node
import uk.gibby.annotation.Relates
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation

@Node
@Relates(to = Movie::class, by = ActedIn::class)
data class Actor(val name: String, val actorId: Long)

@Node
data class Movie(val title: String, val releaseYear: Long)
data class ActedIn(val role: String)


class ActorNode: RedisNode(){
    val name by string()
    val actorId by long()
    val actedIn = relates(ActedInRelation::class)
}
class MovieNode: RedisNode(){
    val title by string()
    val releaseYear by long()
    val movieId by long()
}
class ActedInRelation: RedisRelation<ActorNode, MovieNode>(){
    val role by string()
}
