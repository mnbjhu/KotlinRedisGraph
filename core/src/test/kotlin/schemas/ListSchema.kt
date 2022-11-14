package schemas

import UnitNode
import uk.gibby.redis.results.primitive.LongResult
import uk.gibby.redis.results.primitive.arrayAttribute

class ListNode : UnitNode() {
    val myList by arrayAttribute(::LongResult)
}