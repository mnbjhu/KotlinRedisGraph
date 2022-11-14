package uk.gibby.redis.results.primitive

import uk.gibby.redis.results.ResultValue
import kotlin.reflect.full.primaryConstructor

sealed class PrimitiveResult<T>: ResultValue<T>() {
    private var _value: T? = null

    override var ValueSetter.value: T?
        get() = _value
        set(value){_value = value}
    override var _reference: String? = null
    override fun copyType(): ResultValue<T> =
        this::class.primaryConstructor!!.call()

}