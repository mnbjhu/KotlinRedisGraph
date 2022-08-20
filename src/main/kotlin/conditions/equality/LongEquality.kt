package conditions.equality

import Results.primative.BooleanResult
import Results.primative.LongResult

/**
 * Long equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty Long equality
 */
class LongEquality(private val attribute: LongResult, private val literal: Long): BooleanResult(){
    override fun toString() = "$attribute = $literal"

}
