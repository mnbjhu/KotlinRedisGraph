
open class RedisRelation<T: RedisClass, U: RedisClass>(
    val from: T,
    val to: U
) {

}