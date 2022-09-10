package statements

import results.ResultValue
import scopes.QueryScope

class OrderBy(private val comparable: ResultValue<Any>): Statement() {
    override fun getCommand(): String = "ORDER BY ${comparable.getReferenceString()}"
    companion object{
        fun QueryScope.orderBy(result: ResultValue<Any>){
            commands.add(OrderBy(result))
        }
    }

}