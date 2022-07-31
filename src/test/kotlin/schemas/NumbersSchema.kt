package schemas

import api.RedisNode

class MyNumber(override val instanceName: String) : RedisNode("MyNumber") {
    val num = double("num")
}