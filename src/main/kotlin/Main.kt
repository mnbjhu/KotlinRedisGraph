import api.RedisGraph
data class UserData(val firstName: String, val lastName: String, val age: Long)
fun main() {
    val graph = RedisGraph("SocialGraph", "raspberrypi.local")

    graph.create(User::class){
        firstName["James"]
        lastName["Gibson"]
        age[23]
    }

    val matches = graph.query{
        val me = variableOf<User>("me")
        where = (me.firstName eq "James") and (me.age eq 23)
        result(me.firstName, me.lastName, me.age){
            UserData(me.firstName(), me.lastName(), me.age())
        }
    }

    println(matches)
}

