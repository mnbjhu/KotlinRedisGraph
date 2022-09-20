package uk.gibby.redis.attributes

import uk.gibby.redis.results.Attribute
import uk.gibby.redis.results.StructResult

abstract class StructAttribute<T>: StructResult<T>(), Attribute<T>