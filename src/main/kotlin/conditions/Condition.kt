package conditions

import conditions.logic.And
import conditions.logic.Or

interface Condition {
    override fun toString(): String
    infix fun and(other: Condition) = And(this, other)
    infix fun or(other: Condition) = Or(this, other)
}
