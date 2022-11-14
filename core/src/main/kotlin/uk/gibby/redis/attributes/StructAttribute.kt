package uk.gibby.redis.attributes

import uk.gibby.redis.results.StructResult

abstract class StructAttribute<T>: StructResult<T>(), Attribute<T>