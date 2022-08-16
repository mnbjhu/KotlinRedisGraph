package paths

object RelationCounter {
    private var count = 1
    fun getNext() = "relation${count++}"
}