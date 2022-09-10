package schemas

import uk.gibby.redis.core.RedisNode

class EnumSchema: RedisNode("MyEnum") {
    val enum = serializable<MyEnum>("enum")
}