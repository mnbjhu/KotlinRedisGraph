package uk.gibby.redis.statements

import uk.gibby.redis.results.LongResult
import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.of
import uk.gibby.redis.scopes.QueryScope

class Skip(private val count: ResultValue<Long>): Statement(){
    constructor(long: Long): this(LongResult() of long)
    override fun getCommand(): String {
        return "SKIP ${count.getString()}"
    }

    companion object{
        fun QueryScope.skip(count: ResultValue<Long>) = Skip(count).also { commands.add(it) }
        fun QueryScope.skip(count: Long) = Skip(count).also { commands.add(it) }
    }
}