package schemas

import UnitNode
import uk.gibby.redis.core.RedisNode

class MyNumber: UnitNode() {
    val num by double()
}