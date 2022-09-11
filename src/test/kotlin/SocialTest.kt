import uk.gibby.redis.core.RedisGraph
import org.junit.jupiter.api.Test
import uk.gibby.redis.paths.minus
import uk.gibby.redis.results.result
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.paths.minus

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
