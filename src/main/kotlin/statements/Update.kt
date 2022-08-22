package statements

class Update(private val update: String): Statement() {
    override fun getCommand(): String = "SET $update"
}