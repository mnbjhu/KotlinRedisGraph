package api

abstract class RedisRelation<out T: RedisNode, out U: RedisNode>(
    val from: T,
    val to: U,
    override val typeName: String
): WithAttributes() {
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
    abstract override val instanceName: String
}


