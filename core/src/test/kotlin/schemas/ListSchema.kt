package schemas

import UnitNode
import uk.gibby.redis.results.StringResult
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.results.arrayAttribute

class ListNode : UnitNode() {
    val myList by arrayAttribute(::StringResult)
}