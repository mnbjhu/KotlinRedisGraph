import api.RedisClass
import api.RedisGraph
import api.RedisRelation
import api.create
import scopes.CreatePathScope

fun main() {
    val queryText = RedisGraph.query{
        val photo = create<Photo>("photo")
        val (familyMember) = photo.tagged("familyMember")
        val (me, friendRelation) = familyMember.friendsWith("me")
        where = (me.firstName eq "James") and (me.lastName eq "Gibson") and friendRelation.isFamily
        create {
            familyMember.sharedPhotos("f") - photo
        }
        result(familyMember.firstName, familyMember.lastName, photo.imageName)
    }
    println(queryText)

    val createQuery = create<User> {
        firstName["James"]
        lastName["Gibson"]
        age[23]
    }
    println(createQuery)
}
class User(override val instanceName: String): RedisClass("User"){
    val firstName = string("firstName")
    val lastName = string("lastName")
    val age = int("age")
    val friendsWith = relates(FriendsWith::class)
    val sharedPosts = relates(SharedPosts::class)
    val sharedPhotos = relates(SharedPhotos::class)
}
class FriendsWith(from: User, to: User, override val instanceName: String):
    RedisRelation<User, User>(from, to, "FRIENDS_WITH"){
        val isFamily = boolean("isFamily")
    }
class SharedPosts(
    from: User,
    to: Post,
    override val instanceName: String
    ): RedisRelation<User, Post>(from, to, "SHARED_POSTS")
class SharedPhotos(
    from: User,
    to: Photo,
    override val instanceName: String
): RedisRelation<User, Photo>(from, to, "SHARED_PHOTOS")

class Post(override val instanceName: String): RedisClass("Post"){
    val title = string("title")
    val content = string("content")
    val likes = string("likes")
    val linkedPhoto = relates(LinkedPhoto::class)
}
class LinkedPhoto(
    from: Post,
    to: Photo,
    override val instanceName: String
): RedisRelation<Post, Photo>(from, to, "LINKED_PHOTO")

class Photo(override val instanceName: String): RedisClass("Photo"){
    val imageName = string("imageName")
    val likes = int("likes")
    val tagged = relates(TaggedIn::class)
}
class TaggedIn(
    from: Photo,
    to: User,
    override val instanceName: String
): RedisRelation<Photo, User>(from, to, "TAGGED_IN")
