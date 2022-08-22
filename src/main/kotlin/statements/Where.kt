package statements

import results.primative.BooleanResult

class Where(private val predicate: BooleanResult): Statement() {
    override fun getCommand(): String = "WHERE $predicate"
}