package schemas

import api.RedisNode

class ListNode(override val instanceName: String) : RedisNode("ListNode") {
    val myList = stringList("my_list")
}