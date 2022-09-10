package schemas

import uk.gibby.redis.core.RedisNode


class ListNode : RedisNode("ListNode") {
    val myList = array(string(),"my_list")
}