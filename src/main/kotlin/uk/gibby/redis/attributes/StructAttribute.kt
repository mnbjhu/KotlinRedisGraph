package uk.gibby.redis.attributes

import uk.gibby.redis.core.*
import kotlin.reflect.KProperty

abstract class StructAttribute<T>: Attribute<T>(),  StructResult<T>{
    override var parent: AttributeParent? = null
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
    override var name: String = ""
    override var instanceName: String
        get() = name; set(_) {}
}