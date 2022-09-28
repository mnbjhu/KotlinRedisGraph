package schemas

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.UnitNode

class EnumSchema: UnitNode() {
    val enum by serializable<MyEnum>()
}