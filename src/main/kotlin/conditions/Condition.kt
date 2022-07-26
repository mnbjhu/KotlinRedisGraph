package conditions

import conditions.logic.And

interface Condition {
    override fun toString(): String
    infix fun and(other: Condition) = And(this, other)
}
