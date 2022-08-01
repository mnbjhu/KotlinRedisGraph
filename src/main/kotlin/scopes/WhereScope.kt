package scopes

import attributes.DoubleAttribute
import attributes.LongAttribute
import attributes.StringAttribute
import conditions.equality.DoubleEquality
import conditions.equality.LongEquality
import conditions.equality.StringEquality

class WhereScope {
    infix fun LongAttribute.eq(literal: Long) = LongEquality(this, literal)
    infix fun StringAttribute.eq(literal: String) = StringEquality(this, literal)
    infix fun DoubleAttribute.eq(literal: Double) = DoubleEquality(this, literal)
}