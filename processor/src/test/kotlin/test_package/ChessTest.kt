package test_package

import uk.gibby.redis.annotation.Node
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.annotation.Relates
import uk.gibby.redis.core.RedisGraph


class ChessTest {
    private val graph = RedisGraph(
        name = "chess_test",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )

}

@RedisType
class Empty

@Node
@Relates(to = Space::class, by = "contains", data = Empty::class)
data class Board(val turn: Piece.Color)

@Node
data class Space(
    val position: Vector2,
    val holds: Piece
)

@RedisType
data class Piece(
    val type: Type,
    val color: Color,
    val moves: List<Move>
){
    enum class Color{
        White,
        Black
    }
    enum class Type{
        Empty,
        Pawn,
        Rook,
        Knight,
        Bishop,
        Queen,
        King
    }
}

@RedisType
data class Move(
    val direction: Vector2,
    val iterations: Long,
    val type: Type
){
    enum class Type{
        Attacking,
        Passive,
        Any,
    }
}

@RedisType
data class Vector2(val x: Long, val y: Long)