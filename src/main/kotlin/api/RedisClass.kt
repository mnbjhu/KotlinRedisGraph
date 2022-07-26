package api

import attributes.Attribute
import conditions.equality.DoubleEquality
import conditions.equality.IntEquality
import conditions.equality.StringEquality

abstract class RedisClass(
    override val typeName: String
): HasAttributes {
    override val attributes: MutableList<Attribute> = mutableListOf()
    private val relations: MutableList<RedisRelation<RedisClass, RedisClass>> = mutableListOf()

}