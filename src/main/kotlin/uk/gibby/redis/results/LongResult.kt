package uk.gibby.redis.results

/**
 * Long result
 *
 * @constructor Create empty Long result
 */
open class LongResult: PrimitiveResult<Long>(){
    companion object {
        val Long.result
            get() = LongResult().also { it.value = this }
        val Int.result
            get() = LongResult().also { it.value = this.toLong() }
    }
}