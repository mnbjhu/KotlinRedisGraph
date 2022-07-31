import api.RedisNode
import api.RedisRelation

class User(override val instanceName: String): RedisNode("User"){
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

class Post(override val instanceName: String): RedisNode("Post"){
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

class Photo(override val instanceName: String): RedisNode("Photo"){
    val imageName = string("imageName")
    val likes = int("likes")
    val tagged = relates(TaggedIn::class)
}
class TaggedIn(from: Photo, to: User, override val instanceName: String): RedisRelation<Photo, User>(from, to, "TAGGED_IN")
