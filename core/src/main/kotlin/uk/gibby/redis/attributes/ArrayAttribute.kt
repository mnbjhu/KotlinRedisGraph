package uk.gibby.redis.attributes

import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.ResultBuilder
import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KFunction0

class ArrayAttribute<T, U: ResultValue<T>>(type: ResultBuilder<T, U>): ArrayResult<T, U>(type), Attribute<List<T>>{
    constructor(type: KFunction0<U>): this(ResultBuilder { type() })
}