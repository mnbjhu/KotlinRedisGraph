package uk.gibby.redis.statements

import uk.gibby.redis.results.LongResult
import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.results.of
import uk.gibby.redis.scopes.QueryScope

class Limit(private val count: ResultValue<Long>): Statement(){
    constructor(long: Long): this(LongResult() of long)
    override fun getCommand(): String {
        return "LIMIT ${count.getString()}"
    }
    companion object{
        fun QueryScope.limit(count: ResultValue<Long>) = Limit(count).also { commands.add(it) }
        fun QueryScope.limit(count: Long) = Limit(count).also { commands.add(it) }
    }
}