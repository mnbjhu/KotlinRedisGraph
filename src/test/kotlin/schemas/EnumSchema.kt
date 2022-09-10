package schemas

import core.RedisNode

class EnumSchema: RedisNode("MyEnum") {
    val enum = serializable<MyEnum>("enum")
}