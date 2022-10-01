
package chess

import Vector2
import Vector2Attribute
import uk.gibby.redis.core.*
import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.ResultScope
import uk.gibby.redis.results.StructResult
import uk.gibby.redis.results.array

val forwards = Vector2(0, 1)
val backwards = Vector2(0, -1)
val left = Vector2(-1, 0)
val right = Vector2(1, 0)

val forwardsDiagonalDirections = listOf(
    forwards + left,
    forwards + right
)
val backwardsDiagonalDirections = listOf(
    backwards + left,
    backwards + right
)
val diagonalDirections = forwardsDiagonalDirections + backwardsDiagonalDirections

val rankDirections = listOf(left, right)
val fileDirections = listOf(forwards, backwards)

val fileRankDirections = fileDirections + rankDirections
val allDirections = diagonalDirections + fileDirections

val knightDirections = listOf(
    (2 * forwards) + left,
    (2 * backwards) + left,
    (2 * forwards) + right,
    (2 * backwards) + right,
    (2 * left) + forwards,
    (2 * left) + backwards,
    (2 * right) + forwards,
    (2 * right) + backwards,
)

private operator fun Int.times(vector: Vector2) = Vector2(this * vector.x, this * vector.y)
fun List<Vector2>.anyDistance() = map{ PieceType.Move(PieceType.Move.Type.ToAny, it, Long.MAX_VALUE) }
fun List<Vector2>.oneSpace() = map{ PieceType.Move(PieceType.Move.Type.ToAny, it, 1) }

enum class PieceColour{
    White,
    Black,
}
enum class PieceType(
    val moves: List<Move>
){
    Pawn(
        forwardsDiagonalDirections.map { Move(Move.Type.TakePiece, it, 1) } +
        Move(Move.Type.ToEmpty, forwards, 1)
    ),
    StartPawn(Pawn.moves + Move(Move.Type.ToEmpty, forwards, 2)),
    Rook(fileRankDirections.anyDistance()),
    Knight(knightDirections.oneSpace()),
    Bishop(diagonalDirections.anyDistance()),
    Queen(allDirections.anyDistance()),
    King(allDirections.oneSpace())
    ;
    constructor(vararg moves: Move): this(moves.toList())
    class Move(
        val type: Type,
        val direction: Vector2,
        val maxIterations: Long
    ){
        enum class Type{
            TakePiece,
            ToEmpty,
            ToAny
        }
    }
}

class MoveResult: StructResult<PieceType.Move>(){
    val type by serializable<PieceType.Move.Type>()
    val direction by Vector2Attribute()
    val maxIterations by long()
    override fun ResultScope.getResult() = PieceType.Move(!type, !direction, !maxIterations)
    override fun ParamMap.setResult(value: PieceType.Move) {
        type[value.type]
        direction[value.direction]
        maxIterations[value.maxIterations]
    }
}


open class PieceTypeResult: StructResult<PieceType>(){
    val type by serializable<PieceType>()
    val moves by array(::MoveResult)
    override fun ResultScope.getResult(): PieceType = !type
    override fun ParamMap.setResult(value: PieceType) {
        type[value]
        moves[value.moves]
    }
}
class PieceTypeAttribute: PieceTypeResult(), Attribute<PieceType>
class PieceNode: CreatableNode<Piece>(){
    val colour by serializable<PieceColour>()
    val type by PieceTypeAttribute()
    override fun NodeScope.getResult() = Piece(!colour, !type)
    override fun ParamMap.setArgs(value: Piece){
        type[value.type]
        colour[value.colour]
    }
}

data class Piece(
    val colour: PieceColour,
    val type: PieceType
)

