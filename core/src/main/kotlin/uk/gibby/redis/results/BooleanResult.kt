package uk.gibby.redis.results

/**
 * Boolean result
 *
 * @constructor Create empty Boolean result
 */
open class BooleanResult : PrimitiveResult<Boolean>() {


    override fun parse(result: Iterator<Any?>): Boolean {
        return result.next() as Boolean
    }
    /**
     * And
     *
     * @param other
     */
    infix fun and(other: BooleanResult) = BooleanResult().also{
        it._reference = "(${getString()}) AND (${other.getString()})"
    }

    /**
     * Or
     *
     * @param other
     */
    infix fun or(other: BooleanResult) = BooleanResult().also{
        it._reference = "(${getString()}) OR (${other.getString()})"
    }
    operator fun not() = BooleanResult().also{
        it._reference = "NOT(${getString()})"
    }
}
