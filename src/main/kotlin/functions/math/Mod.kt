package functions.math

import results.primative.BooleanResult
import results.primative.DoubleResult
import results.primative.LongResult

operator fun LongResult.rem(other: LongResult) = object: LongResult{
    override fun getReferenceString() = "(${this@rem} % $other)"
}
operator fun LongResult.rem(literal: Long) = object: LongResult{
    override fun getReferenceString() = "(${this@rem} % $literal)"
}
operator fun DoubleResult.rem(other: DoubleResult) = object: DoubleResult{
    override fun getReferenceString() = "(${this@rem} % $other)"
}
operator fun DoubleResult.rem(literal: Double) = object: DoubleResult{
    override fun getReferenceString() = "(${this@rem} % $literal)"
}