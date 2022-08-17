package schemas

import User
import api.RedisGraph
import functions.relation.startNode
import org.junit.jupiter.api.Test
import paths.minus


class SocialTest {
    @Test
    fun socialTest(){
        val socialGraph = RedisGraph("social", "localhost")
        socialGraph.query {
            val (x, r) = match(User() - { +friendsWith } - User() - { friendsWith } - User() - { friendsWith } - User())
            where(startNode(r).age eq 1)
            result(x.age)
        }
    }
}

