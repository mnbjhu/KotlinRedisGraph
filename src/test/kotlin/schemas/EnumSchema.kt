package schemas

import MyEnum
import api.RedisNode

class EnumSchema: RedisNode("MyEnum") {
    val enum = serializable<MyEnum>("enum")
}