import Photo
import Post
import User
import api.RedisGraph
import conditions.True
import functions.relation.startNode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import paths.minus


class SocialTest {
    @Test
    fun socialTest(){
        val socialGraph = RedisGraph("social", "localhost")
        socialGraph.query {
            val (_, familyMember, post, photo) = match(
                User() - { +friendsWith{ isFamily[true] } } - User()
                        - { sharedPosts } - Post() - { linkedPhoto } - Photo()
            ).nodes()
            result(familyMember.firstName, familyMember.lastName, post.title, photo.imageName)
        }
    }
}
