package schemas

import api.RedisNode

class ListNode : RedisNode("ListNode") {
    val myList = stringList("my_list")
}