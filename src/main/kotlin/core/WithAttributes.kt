package core

import attributes.*
import attributes.primative.BooleanAttribute
import attributes.primative.DoubleAttribute
import attributes.primative.LongAttribute
import attributes.primative.StringAttribute
import paths.NameCounter
import results.ResultValue

/**
 * With attributes
 *
 * @constructor Create empty With attributes
 */
sealed class WithAttributes {
    var params: List<ParameterPair<*>>? = null
    internal abstract val typeName: String
    internal abstract val attributes: MutableList<Attribute<*>>
    var instanceName = NameCounter.getNext()
    /**
     * String
     *
     * @param name
     */
    protected fun string(name: String? = null) = if(name != null) StringAttribute(name, this) else StringAttribute("", null)

    /**
     * Long
     *
     * @param name
     */
    protected fun long(name: String? = null) = if(name != null) LongAttribute(name, this) else LongAttribute("", null)

    /**
     * Double
     *
     * @param name
     */
    protected fun double(name: String? = null) = if(name != null) DoubleAttribute(name, this) else DoubleAttribute("", null)

    /**
     * Boolean
     *
     * @param name
     */
    protected fun boolean(name: String? = null) = if(name != null) BooleanAttribute(name, this) else BooleanAttribute("", null)
    protected fun <T>array(type: Attribute<T>, name: String? = null) = if(name == null) ArrayAttribute("", type, null)
        else ArrayAttribute(name, type, this)
    protected inline fun <reified T: Any>serializable(name: String) = SerializableAttribute(name, this, T::class)


}
class ParamMap{
    private val map = mutableListOf<ParameterPair<*>>()
    fun getParams() = map.toList()
    operator fun <T: Any?>set(attribute: Attribute<T>, value: T){
        map.add(attribute to value)
    }
}

operator fun <T: WithAttributes>T.invoke(scope: T.(ParamMap) -> Unit) {
    val params = ParamMap()
    scope(params)
    this.params = params.getParams()
}
