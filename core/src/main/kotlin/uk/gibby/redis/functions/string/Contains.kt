package uk.gibby.redis.functions.string

import uk.gibby.redis.results.primitive.BooleanResult
import uk.gibby.redis.results.primitive.StringResult
import uk.gibby.redis.results.literalOf

/**
 * Contains
 *
 * @property hayStack
 * @property needle
 * @constructor Create empty Contains
 */
class Contains(private val hayStack: StringResult, private val needle: StringResult): BooleanResult() {
    override var _reference: String? = "(${hayStack.getString()} contains ${needle.getString()})"
    companion object{
        @JvmStatic
        infix fun StringResult.contains(needle: String) = Contains(this, literalOf(StringResult(), needle))
        @JvmStatic
        infix fun StringResult.contains(needle: StringResult) = Contains(this, needle)
    }
}