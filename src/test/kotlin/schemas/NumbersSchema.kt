package schemas

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.UnitNode
import uk.gibby.redis.core.double

class MyNumber: UnitNode() {
    val num by double()
}