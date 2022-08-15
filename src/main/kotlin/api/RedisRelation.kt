package api

import attributes.Attribute

/**
 * Redis relation
 *
 * @param T
 * @param U
 * @property from
 * @property to
 * @property typeName
 * @constructor Create empty Redis relation
 */
abstract class RedisRelation<out T: RedisNode, out U: RedisNode>(
    val from: T,
    val to: U,
    override val typeName: String
): WithAttributes() {
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
    abstract override val instanceName: String
    override fun toString(): String {
        val attrs = attributes.mapNotNull {
            if(it is ResultValue<*>){
                when(it.value){
                    null -> null
                    is String -> "${it.name}: '${it.value}'"
                    else -> "${it.name}: ${it.value}"
                }
            }
            else throw Exception("Uh oh")
        }.joinToString()
        return "[$instanceName:$typeName{$attrs}]"
    }
}


