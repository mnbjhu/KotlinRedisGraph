package schemas

import uk.gibby.redis.core.RedisNode

class EnumSchema: RedisNode() {
    val enum by serializable<MyEnum>()
}