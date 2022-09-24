package uk.gibby.redis.attributes

import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.ResultValue

class ArrayAttribute<T, U: ResultValue<T>>(type: U): ArrayResult<T, U>(type), Attribute<List<T>>