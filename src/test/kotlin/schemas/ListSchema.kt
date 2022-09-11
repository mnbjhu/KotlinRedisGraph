package schemas

import uk.gibby.redis.core.RedisNode

class ListNode : RedisNode() {
    val myList by array(string())
}