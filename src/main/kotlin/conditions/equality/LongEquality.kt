package conditions.equality

import results.primative.BooleanResult
import results.primative.LongResult

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
