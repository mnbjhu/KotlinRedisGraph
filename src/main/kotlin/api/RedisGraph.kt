package api

import scopes.QueryScope

class RedisGraph {
    companion object{
        @JvmStatic
        fun query(action: QueryScope.() -> Unit): String{
            val scope = QueryScope()
            scope.action()
            return scope.toString()
        }
    }
}