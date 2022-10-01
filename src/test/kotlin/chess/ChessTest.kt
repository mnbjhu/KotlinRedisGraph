package chess

import Vector2
import uk.gibby.redis.core.WithAttributes
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.core.invoke
import uk.gibby.redis.functions.array.range
import uk.gibby.redis.results.array
import uk.gibby.redis.statements.Create.Companion.create


class ChessTest {
    val chessGraph = RedisGraph(
        "ChessGraphTest",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    fun createBoard(): Int{
        chessGraph.create(BoardNode::class)
        chessGraph.query {
            val x = unwind(range(1, 8))
            val y = unwind(range(1, 8))
            create(::SpaceNode.invoke {
                //it[pieceId] = x; it[position] = ::Vector2{  }
            })
        }
        TODO()
    }
}