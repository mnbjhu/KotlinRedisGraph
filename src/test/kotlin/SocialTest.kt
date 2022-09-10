import core.RedisGraph
import org.junit.jupiter.api.Test
import paths.minus
import results.result
import schemas.*


class SocialTest {
    @Test
    fun socialTest(){
        val socialGraph = RedisGraph("social",
            host = TestAuth.host,
            port = TestAuth.port,
            password = TestAuth.password
        )
        socialGraph.query {
            val (_, familyMember, post, photo) = match(
                User() - { +friendsWith{ it[isFamily] = true } } - User()
                        - { sharedPosts } - Post() - { linkedPhoto } - Photo()
            ).nodes()
            result(familyMember.firstName, familyMember.lastName, post.title, photo.imageName)
        }
    }
}
