package results.array

import results.ArrayResult
import results.ResultValue
import results.primative.LongResult

abstract class LongArrayResult: ArrayResult<Long> {
    abstract val name: String
    override fun getReferenceString() = name
    override val type: ResultValue<Long> = object: LongResult{
        override fun getReferenceString(): String = ""
    }
}