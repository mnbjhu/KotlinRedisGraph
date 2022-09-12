package uk.gibby.redis.results.primative

import uk.gibby.redis.results.ResultValue

interface StringResult : ResultValue<String> {
    override fun getLiteral(value: String) = "'$value'" }