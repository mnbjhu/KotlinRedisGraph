package uk.gibby.redis.statements

sealed class Statement {
    abstract fun getCommand(): String

}