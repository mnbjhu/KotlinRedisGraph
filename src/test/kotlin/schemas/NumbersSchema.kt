package schemas

import uk.gibby.redis.core.RedisNode

class MyNumber: RedisNode() {
    val num by double()
}