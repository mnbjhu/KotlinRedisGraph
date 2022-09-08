package schemas

import core.RedisNode

class ListNode : RedisNode("ListNode") {
    val myList = stringList("my_list")
}