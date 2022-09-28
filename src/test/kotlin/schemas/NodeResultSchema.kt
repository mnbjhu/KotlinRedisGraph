package schemas

import uk.gibby.redis.core.NodeScope
import uk.gibby.redis.core.RedisNode

class NodeWithResult: RedisNode<Long>() {
    val number by long()
    override fun NodeScope.getResult() = !number

}