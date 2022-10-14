package schemas

import uk.gibby.redis.attributes.primative.LongAttribute
import uk.gibby.redis.attributes.primative.StringAttribute
import uk.gibby.redis.core.NodeResult
import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisNode

data class NodeTestData(val data: String, val number: Long)
class ReturningNode: RedisNode<NodeTestData>() {
    val data by StringAttribute()
    val number by LongAttribute()
    override fun NodeResult.getResult() = NodeTestData(!data, !number)

}