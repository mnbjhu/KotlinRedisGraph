package schemas

import core.RedisNode

class ListNode : RedisNode("ListNode") {
    val myList = array(string(),"my_list")
}