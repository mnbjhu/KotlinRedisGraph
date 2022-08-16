package schemas

import api.RedisNode

class MyNumber() : RedisNode("MyNumber") {
    val num = double("num")
}