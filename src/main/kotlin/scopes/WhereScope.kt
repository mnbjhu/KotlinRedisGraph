package scopes

import api.ResultValue
import attributes.DoubleAttribute
import attributes.LongAttribute
import attributes.StringAttribute
import conditions.array.Contains
import conditions.equality.DoubleEquality
import conditions.equality.LongEquality
import conditions.equality.StringEquality

class WhereScope {
    infix fun LongAttribute.eq(literal: Long) = LongEquality(this, literal)
    infix fun StringAttribute.eq(literal: String) = StringEquality(this, literal)
    infix fun DoubleAttribute.eq(literal: Double) = DoubleEquality(this, literal)

    infix fun <T>ResultValue.ArrayResult<T>.contains(literal: T) = Contains(this, literal)
}