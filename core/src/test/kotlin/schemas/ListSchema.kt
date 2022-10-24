package schemas

import UnitNode
import uk.gibby.redis.results.LongResult
import uk.gibby.redis.results.arrayAttribute

class ListNode : UnitNode() {
    val myList by arrayAttribute(::LongResult)
}