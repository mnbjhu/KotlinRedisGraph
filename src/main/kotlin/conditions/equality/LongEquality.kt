package conditions.equality

import api.ResultValue

/**
 * Long equality
 *
 * @property attribute
 * @property literal
 * @constructor Create empty Long equality
 */
class LongEquality(val attribute: LongResult, private val literal: Long): ResultValue.BooleanResult(){
    override fun toString() = "$attribute = $literal"
}