package conditions

abstract class Condition {
    abstract override fun toString(): String
    object None : Condition() {
        override fun toString() = ""
    }

}
