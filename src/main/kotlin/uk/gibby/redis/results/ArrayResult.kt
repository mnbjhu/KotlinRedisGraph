package uk.gibby.redis.results

interface ArrayResult<T> : ResultValue<List<T>> {
    val type: ResultValue<T>
    override fun parse(result: Iterator<Any?>): List<T> {
        val values = (result.next() as List<*>)
        val innerIter = values.iterator()
        return values.map { type.parse(innerIter) }
    }
    override fun getLiteral(value: List<T>) = "[${value.joinToString { type.getLiteral(it) }}]"
}