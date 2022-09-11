package uk.gibby.redis.conditions

import uk.gibby.redis.results.primative.BooleanResult

object True : BooleanResult {
    override fun getReferenceString() = ""
}