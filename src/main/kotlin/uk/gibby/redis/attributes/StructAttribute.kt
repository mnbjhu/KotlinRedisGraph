package uk.gibby.redis.attributes

import uk.gibby.redis.core.*
import kotlin.reflect.KProperty

abstract class StructAttribute<T>: Attribute<T>(),  StructResult<T>{
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
    override var name: String = ""
    operator fun <T, U: Attribute<T>>U.getValue(thisRef: Any?, property: KProperty<*>): U{
        name = "${this@StructAttribute.name}[${this@StructAttribute.attributes.size}]"
        parent = this@StructAttribute.parent
        return this
    }
    override fun getLiteralString(value: T): String{
        val params = ParamMap()
            .apply { setResult(value, this) }
            .getParams()
        //if(attributes.size != params.size) throw Exception("All params are required when creating a literal string")
        return "[${params.joinToString { (it as ParameterPair<Any>).getLiteralString() }}]"
    }
    abstract fun setResult(result: T, params: ParamMap)
}