fun main() {
    //val object2 = object1.secondObj

}
class TestClass: RedisClass("test_class"){
    val x = string("my_string")
    val y = int("my_int")
    //val other by RedisRelation(TestClass2())
}
class TestClass2: RedisClass("Test"){
    //val other by RedisRelation(TestClass())
}
