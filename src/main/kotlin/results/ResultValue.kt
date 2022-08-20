package results

/**
 * Result value
 *
 * @param T
 * @constructor Create empty Result value
 */
abstract class ResultValue<T> {
    var value: T? = null
    abstract override fun toString(): String
    open fun set(data: Any?) {

        value = data as T
    }
    /**
     * Get
     *
     * @param newValue
     */
    open operator fun get(newValue: T){
        value = newValue
    }

    /**
     * Invoke
     *
     * @return
     */
    operator fun invoke(): T = if(value != null) value!! else throw Exception("attribute has not been returned")

}