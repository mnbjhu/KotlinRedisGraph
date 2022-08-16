package paths

object NameCounter {
    private var count = 1
    fun getNext() = "o${count++}"
}