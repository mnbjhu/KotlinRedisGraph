package core

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
    var isMultipleRelation = false
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
    fun getMatchString(): String {
        return "[$instanceName:$typeName${if(isMultipleRelation) "*" else ""}{${params?.joinToString { (it as ParameterPair<Any?>).getLocalEqualityString()} ?: ""}}]"
    }
    fun getCreateString(): String {
        if((params?.size ?: 0) != attributes.size) throw Exception("Relations should be created with all parameters (${attributes.size} attributes) found ${params?.size ?: 0}")
        return "[:$typeName{${params?.joinToString { (it as ParameterPair<Any?>).getLocalEqualityString() } ?: ""}}]"
    }
}


