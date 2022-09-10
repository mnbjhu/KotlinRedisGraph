package results

import scopes.QueryScope

fun <T>QueryScope.result(vararg values: ResultValue<T>) = object: ResultValue<List<T>>{
    override fun parse(result: Iterator<Any?>): List<T> {
        return values.map { it.parse(result) }
    }
    override fun getReferenceString(): String {
        return values.joinToString { it.getReferenceString() }
    }

}