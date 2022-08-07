package conditions.array

import api.ResultValue

class Contains<T>(val attribute: ArrayResult<T>, private val element: T): ResultValue.BooleanResult() {
    override fun toString(): String = "($attribute contains ${if(element is String) "'$element'" else "$element"})"
    companion object{
        fun <T>ArrayResult<T>.contains(element: T) = Contains(this, element)
    }
}