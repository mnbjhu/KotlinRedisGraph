package schemas

import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.array
import uk.gibby.redis.core.string

class ListNode : RedisNode() {
    val myList by array(string())
}