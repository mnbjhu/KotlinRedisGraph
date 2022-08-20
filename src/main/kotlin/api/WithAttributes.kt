package api

import attributes.*
import attributes.array.DoubleArrayAttribute
import attributes.array.LongArrayAttribute
import attributes.array.StringArrayAttribute
import attributes.primative.BooleanAttribute
import attributes.primative.DoubleAttribute
import attributes.primative.LongAttribute
import attributes.primative.StringAttribute
import paths.NameCounter

/**
 * With attributes
 *
 * @constructor Create empty With attributes
 */
sealed class WithAttributes {

    abstract val typeName: String
    abstract val attributes: MutableList<Attribute<*>>
    var instanceName = NameCounter.getNext()

    /**
     * String
     *
     * @param name
     */
    fun string(name: String) = StringAttribute(name, this)

    /**
     * Long
     *
     * @param name
     */
    fun long(name: String) = LongAttribute(name, this)

    /**
     * Double
     *
     * @param name
     */
    fun double(name: String) = DoubleAttribute(name, this)

    /**
     * Boolean
     *
     * @param name
     */
    fun boolean(name: String) = BooleanAttribute(name, this)

    /**
     * Long list
     *
     * @param name
     */
    fun longList(name: String) = LongArrayAttribute(name, this)

    /**
     * Double list
     *
     * @param name
     */
    fun doubleList(name: String) = DoubleArrayAttribute(name, this)

    /**
     * String list
     *
     * @param name
     */
    fun stringList(name: String) = StringArrayAttribute(name, this)

    /**
     * Boolean list
     *
     * @param name
     */
    fun booleanList(name: String) = BooleanArrayAttribute(name, this)

    inline fun <reified T: Any>serializable(name: String) = SerializableAttribute(name, this, T::class)
}