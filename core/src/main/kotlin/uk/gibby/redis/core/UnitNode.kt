import uk.gibby.redis.core.NodeResult
import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisNode

abstract class UnitNode: RedisNode<Unit>(){
    override fun NodeResult.getResult() {}
    override fun setResult(params: ParamMap, value: Unit) {}

}