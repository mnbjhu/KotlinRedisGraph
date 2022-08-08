package conditions.array

import api.ResultValue

class Contains<T>(val attribute: ArrayResult<T>, private val element: T): ResultValue.BooleanResult() {
    override fun toString(): String = "(${if(element is String) "'$element'" else "$element"} IN $attribute)"
    companion object{
        infix fun <T>ArrayResult<T>.contains(element: T) = Contains(this, element)
    }
}