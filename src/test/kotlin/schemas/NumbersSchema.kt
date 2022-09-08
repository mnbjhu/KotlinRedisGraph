package schemas

import core.RedisNode

class MyNumber() : RedisNode("MyNumber") {
    val num = double("num")
}