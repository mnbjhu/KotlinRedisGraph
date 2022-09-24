package schemas

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.double

class MyNumber: RedisNode() {
    val num by double()
}