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
abstract class RedisRelation<T: RedisNode, U: RedisNode>(
    override val typeName: String
): WithAttributes() {
    lateinit var from: T
    lateinit var to: U
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
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


