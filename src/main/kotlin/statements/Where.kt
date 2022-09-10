package statements

import results.primative.BooleanResult
import scopes.QueryScope

class Where(private val predicate: BooleanResult): Statement() {
    override fun getCommand(): String = "WHERE ${predicate.getReferenceString()}"
    companion object{
        fun QueryScope.where(predicate: BooleanResult){
            commands.add(Where(predicate))
        }
    }
}