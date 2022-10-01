package chess

import Vector2Attribute
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.core.UnitNode

class BoardNode: UnitNode(){
    val spaces = relates(ContainsSpace::class)
}
class ContainsSpace: RedisRelation<BoardNode, SpaceNode>()

open class SpaceNode: UnitNode() {
    val position by Vector2Attribute()
    val pieceId by long()

}
class CanMoveTo: RedisRelation<SpaceNode, SpaceNode>(){
    val pieceId by long()
}


