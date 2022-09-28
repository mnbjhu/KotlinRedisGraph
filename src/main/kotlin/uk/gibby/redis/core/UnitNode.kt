package uk.gibby.redis.core


abstract class UnitNode: RedisNode<Unit>() {
    override fun NodeScope.getResult() = Unit
}