package conditions.array

import results.ArrayResult
import results.primative.BooleanResult

/**
 * Contains
 *
 * @param T
 * @property attribute
 * @property element
 * @constructor Create empty Contains
 */
class Contains<T>(val attribute: ArrayResult<T>, private val element: T): BooleanResult {
    override fun getReferenceString(): String = "(${if(element is String) "'$element'" else "$element"} IN ${attribute.getReferenceString()})"
    companion object{
        infix fun <T> ArrayResult<T>.contains(element: T) = Contains(this, element)
    }
}