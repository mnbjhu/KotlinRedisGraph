package schemas

import api.RedisNode
import scopes.QueryScope

enum class PieceColour{
    White,
    Black,
}
enum class PieceType{
    Pawn,
    Rook,
    Knight,
    Bishop,
    Queen,
    King,
}

class Board: RedisNode("Board"){

    companion object{
        fun QueryScope<Unit>.setupChessBoard(){
            create()
        }
    }
}
class Piece: RedisNode("Piece"){
    val colour = serializable<PieceColour>("colour")
    val type = serializable<PieceType>("type")


}