package schemas

import uk.gibby.redis.core.RedisNode

class MyNumber() : RedisNode("MyNumber") {
    val num = double("num")
}