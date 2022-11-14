package uk.gibby.redis.results

import kotlin.reflect.full.primaryConstructor

abstract class AliasResult<T, U: ResultValue<T>, R>: ResultValue<R>() {
    abstract fun decode(encoded: T): R
    abstract fun encode(data: R): T
    var _value: R? = null
    override var ValueSetter.value: R?
        get() = _value
        set(value){_value = value}

    override fun copyType(): ResultValue<R> {
        return this::class.primaryConstructor!!.call()
    }

    override var _reference: String? = null

}