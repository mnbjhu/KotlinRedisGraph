package uk.gibby.redis.attributes

import uk.gibby.redis.core.*
import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KProperty

abstract class StructAttribute<T>: Attribute<T>(),  StructResult<T>{
    override val attributes: MutableList<Attribute<*>> = mutableListOf()
    override var name: String = ""
    operator fun <T, U: ResultValue<T>>U.getValue(thisRef: Any?, property: KProperty<*>): U{
        name = "${this@StructAttribute.name}[${this@StructAttribute.attributes.size}]"
        parent = this@StructAttribute.parent
        return this
    }
    override fun getLiteralString(value: T): String{
        val params = ParamMap()
            .apply { setResult(value) }
            .getParams()
        //if(attributes.size != params.size) throw Exception("All params are required when creating a literal string")
        return "[${params.joinToString { (it as ParameterPair<Any>).getLiteralString() }}]"
    }
    abstract fun ParamMap.setResult(value: T)
}