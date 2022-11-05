package uk.gibby.redis.statements

import uk.gibby.redis.core.Matchable
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.core.RedisRelation
import uk.gibby.redis.core.WithAttributes
import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.scopes.QueryScope

class WithAs<T, U: ResultValue<T>>(private val variable: U, private val reference: U) : Statement() {
    override fun getCommand(): String = "WITH ${variable.getString()} AS ${reference.getString()}"
    companion object {
        fun <T, U: ResultValue<T>>QueryScope.using(variable: U): U {
            val newReference = variable.copyType().apply { _reference = variable._reference }
            if(newReference is RedisNode) newReference.matched = true
            commands.add(WithAs(variable, newReference))
            return newReference as U
        }
        fun <T, U: ResultValue<T>, A, B: ResultValue<A>>QueryScope.using(var1: U, var2: B): Pair<U, B> {
            val newRef1 = var1.copyType()
            val newRef2 = var2.copyType()
            val command1 = WithAs(var1, newRef1.apply { if(this is RedisNode) matched = true })
            val command2 = WithAs(var2, newRef2.apply { if(this is RedisNode) matched = true })
            val command = MultipleWith(command1, command2)
            commands.add(command)
            return (newRef1 as U) to (newRef2 as B)
        }


    }
    class MultipleWith(private val inner: List<WithAs<*, *>>): Statement(){
        constructor(vararg statements: WithAs<*, *>): this(statements.toList())
        override fun getCommand(): String =
            "WITH ${inner.joinToString { "${ it.variable.getString() } AS ${ it.reference.getString() }" }}"
    }
}
