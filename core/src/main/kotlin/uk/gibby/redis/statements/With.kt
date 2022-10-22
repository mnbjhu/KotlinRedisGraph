package uk.gibby.redis.statements

import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.ResultValue
import uk.gibby.redis.scopes.QueryScope

class WithAs<T, U: ResultValue<T>>(private val variable: U, private val reference: U) : Statement() {
    override fun getCommand(): String = "WITH ${variable.getString()} AS ${reference.getString()}"
    companion object {
        fun <T, U: ResultValue<T>>QueryScope.using(variable: U): U {
            val newReference = variable.copyType()
            commands.add(WithAs(variable, newReference.apply { _reference = NameCounter.getNext() }))
            return newReference as U
        }
    }
}