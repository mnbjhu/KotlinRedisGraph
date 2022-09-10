package conditions.array

import attributes.ArrayAttribute
import attributes.Attribute
import results.ArrayResult
import results.ResultValue
import results.primative.BooleanResult

/**
 * Contains
 *
 * @param T
 * @property attribute
 * @property element
 * @constructor Create empty Contains
 */
class Contains<T>(val attribute: ResultValue<List<T>>, private val element: T): BooleanResult {
    override fun getReferenceString(): String = "(${if(element is String) "'$element'" else "$element"} IN ${attribute.getReferenceString()})"
    companion object{
        infix fun <T> ResultValue<List<T>>.contains(element: T) = Contains(this, element)
    }
}