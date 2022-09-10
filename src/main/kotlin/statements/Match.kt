package statements

import core.Matchable

class Match(private val toMatch: List<Matchable>): Statement() {
    override fun getCommand(): String ="MATCH ${toMatch.joinToString{it.getMatchString()}}"
}