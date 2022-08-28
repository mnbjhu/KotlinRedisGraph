package api

import attributes.*
import attributes.array.BooleanArrayAttribute
import attributes.array.DoubleArrayAttribute
import attributes.array.LongArrayAttribute
import attributes.array.StringArrayAttribute
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

    internal abstract val typeName: String
    internal abstract val attributes: MutableList<Attribute<*>>
    var instanceName = NameCounter.getNext()
    /**
     * String
     *
     * @param name
     */
    protected fun string(name: String) = StringAttribute(name, this)

    /**
     * Long
     *
     * @param name
     */
    protected fun long(name: String) = LongAttribute(name, this)

    /**
     * Double
     *
     * @param name
     */
    protected fun double(name: String) = DoubleAttribute(name, this)

    /**
     * Boolean
     *
     * @param name
     */
     protected fun boolean(name: String) = BooleanAttribute(name, this)

    /**
     * Long list
     *
     * @param name
     */
    protected fun longList(name: String) = LongArrayAttribute(name, this)

    /**
     * Double list
     *
     * @param name
     */
    protected fun doubleList(name: String) = DoubleArrayAttribute(name, this)

    /**
     * String list
     *
     * @param name
     */
    protected fun stringList(name: String) = StringArrayAttribute(name, this)

    /**
     * Boolean list
     *
     * @param name
     */
    protected fun booleanList(name: String) = BooleanArrayAttribute(name, this)
    protected inline fun <reified T: Any>serializable(name: String) = SerializableAttribute(name, this, T::class)
    fun hasAllAttributes() = attributes.all{ (it as ResultValue<*>).value != null }
}