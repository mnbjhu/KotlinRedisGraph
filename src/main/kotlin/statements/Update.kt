package statements

import core.ParameterPair
import core.getGlobalEqualityString
import core.getLocalEqualityString

class Update(private val update: ParameterPair<*>): Statement() {
    override fun getCommand(): String = "SET ${(update as ParameterPair<Any?>).getGlobalEqualityString()}"
}