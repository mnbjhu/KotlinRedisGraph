package schemas

import api.RedisNode

class EnumSchema: RedisNode("MyEnum") {
    val enum = serializable<MyEnum>("enum")
}