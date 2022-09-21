package uk.gibby.redis.results

/**
 * Double result
 *
 * @constructor Create empty Double result
 */
open class DoubleResult : PrimitiveResult<Double>(){
    companion object {
        val Double.result
            get() = DoubleResult().also { it.value = this }
    }
}