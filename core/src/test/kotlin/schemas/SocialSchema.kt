package schemas

import uk.gibby.redis.core.*

class User: RedisNode(){
    val firstName by string()
    val lastName by string()
    val age by long()
    val friendsWith = relates(FriendsWith::class)
    val sharedPosts = relates(SharedPosts::class)
    val sharedPhotos = relates(SharedPhotos::class)
}
class FriendsWith: RedisRelation<User, User>(){
    val isFamily by boolean()
}
class SharedPosts: RedisRelation<User, Post>()
class SharedPhotos: RedisRelation<User, Photo>()

enum class MyEnum{
    A,
    B,
    C,
}

class Post: RedisNode(){
    val title by string()
    val content by string()
    val likes by string()
    val linkedPhoto = relates(LinkedPhoto::class)
}
class LinkedPhoto: RedisRelation<Post, Photo>()

class Photo: RedisNode(){
    val imageName by string()
    val likes by long()
    val tagged = relates(TaggedIn::class)
}
class TaggedIn: RedisRelation<Photo, User>()
