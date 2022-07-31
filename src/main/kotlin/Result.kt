interface RedisResult<T> {
    var value: T?
    operator fun get(newValue: T){
        value = newValue
    }
    operator fun invoke(): T = if(value != null) value!! else throw Exception("attribute has not been returned")
}