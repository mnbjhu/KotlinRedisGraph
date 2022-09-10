package uk.gibby.redis.core

import uk.gibby.redis.attributes.*
import uk.gibby.redis.attributes.primative.BooleanAttribute
import uk.gibby.redis.attributes.primative.DoubleAttribute
import uk.gibby.redis.attributes.primative.LongAttribute
import uk.gibby.redis.attributes.primative.StringAttribute
import uk.gibby.redis.paths.NameCounter

/**
 * With attributes
 *
 * @constructor Create empty With attributes
 */
sealed class WithAttributes {
    var params: List<ParameterPair<*>>? = null
    internal abstract val typeName: String
    internal abstract val attributes: MutableList<uk.gibby.redis.attributes.Attribute<*>>
    var instanceName = NameCounter.getNext()

    /**
     * String
     *
     * @param name
     */
    protected fun string(name: String? = null) =
        if (name != null) StringAttribute(name, this) else StringAttribute("", null)

    /**
     * Long
     *
     * @param name
     */
    protected fun long(name: String? = null) = if (name != null) LongAttribute(name, this) else LongAttribute("", null)

    /**
     * Double
     *
     * @param name
     */
    protected fun double(name: String? = null) =
        if (name != null) DoubleAttribute(name, this) else DoubleAttribute("", null)

    /**
     * Boolean
     *
     * @param name
     */
    protected fun boolean(name: String? = null) =
        if (name != null) BooleanAttribute(name, this) else BooleanAttribute("", null)

    protected fun <T> array(type: uk.gibby.redis.attributes.Attribute<T>, name: String? = null) = if (name == null) uk.gibby.redis.attributes.ArrayAttribute(
        "",
        type,
        null
    )
    else uk.gibby.redis.attributes.ArrayAttribute(name, type, this)

    protected inline fun <reified T : Any> serializable(name: String) = SerializableAttribute(name, this, T::class)


}

class ParamMap {
    private val map = mutableListOf<ParameterPair<*>>()
    fun getParams() = map.toList()
    operator fun <T : Any?> set(attribute: uk.gibby.redis.attributes.Attribute<T>, value: T) {
        map.add(attribute to value)
    }
}

operator fun <T : WithAttributes> T.invoke(scope: T.(ParamMap) -> Unit) {
    val params = ParamMap()
    scope(params)
    this.params = params.getParams()
}
