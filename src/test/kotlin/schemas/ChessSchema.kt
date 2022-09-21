
package schemas

import Vector2Attribute
import uk.gibby.redis.attributes.StructAttribute
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.core.string
import uk.gibby.redis.results.StructResult
import uk.gibby.redis.scopes.QueryScope
import uk.gibby.redis.statements.Create.Companion.create


enum class PieceColour {
    White,
    Black,
}
sealed class PieceType {
    object Pawn: PieceType(){}
    object Rook: PieceType(){}
    object Knight: PieceType(){}
    object Bishop: PieceType(){}
    object Queen: PieceType(){}
    object King: PieceType(){}
}
class Player: RedisNode(){
    val username by string()
    val playingIn = relates(PlayingIn::class)
}
class PlayingIn: RedisRelation<Player, Board>(){
    val asColour by serializable<PieceColour>()
}
class Board: RedisNode(){
    val spaces = relates(ContainsSpace::class)
}
class ContainsSpace: RedisRelation<Board, BoardSpace>()
class BoardSpace: RedisNode(){
    val position by Vector2Attribute()
}
fun QueryScope.createBoard(white: Player, black: Player){

}
