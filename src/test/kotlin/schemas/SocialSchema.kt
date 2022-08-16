import api.RedisNode
import api.RedisRelation

class User: RedisNode("User"){
    val firstName = string("firstName")
    val lastName = string("lastName")
    val age = long("age")
    val friendsWith = relates(FriendsWith::class)
    val sharedPosts = relates(SharedPosts::class)
    val sharedPhotos = relates(SharedPhotos::class)
}
class FriendsWith:
    RedisRelation<User, User>("FRIENDS_WITH"){
    val isFamily = boolean("isFamily")
}
class SharedPosts: RedisRelation<User, Post>("SHARED_POSTS")
class SharedPhotos: RedisRelation<User, Photo>("SHARED_PHOTOS")

class Post: RedisNode("Post"){
    val title = string("title")
    val content = string("content")
    val likes = string("likes")
    val linkedPhoto = relates(LinkedPhoto::class)
}
class LinkedPhoto: RedisRelation<Post, Photo>("LINKED_PHOTO")

class Photo: RedisNode("Photo"){
    val imageName = string("imageName")
    val likes = long("likes")
    val tagged = relates(TaggedIn::class)
}
class TaggedIn: RedisRelation<Photo, User>("TAGGED_IN")
