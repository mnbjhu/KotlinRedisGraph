package schemas

import uk.gibby.redis.results.StringResult
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.results.arrayAttribute

class ListNode : RedisNode() {
    val myList by arrayAttribute(::StringResult)
}