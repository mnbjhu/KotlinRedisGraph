import api.RedisClass
import api.RedisRelation
import api.relates
import attributes.RelationAttribute
import scopes.RedisScope
import kotlin.reflect.KClass

fun main() {
    val createScope = RedisScope()
    with(createScope){
        val (other, otherRelation) =
            TestClass("first").other<TestClass, TestClass2, TestRelation, RelationAttribute<TestClass, TestClass2, TestRelation>>("second")

    }
    println(createScope.paths)
}
class TestClass(override val instanceName: String) : RedisClass("test_class"){
    val x = string("my_string")
    val y = int("my_int")
    val other = relates<TestClass, TestClass2, TestRelation>("other", TestRelation::class)
}
class TestClass2(override val instanceName: String) : RedisClass("Test"){

}
class TestRelation(from: TestClass, to: TestClass2, override val instanceName: String):
    RedisRelation<TestClass, TestClass2>(from, to, "testRelation"){

    }
