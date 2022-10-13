package schemas

import UnitNode
import uk.gibby.redis.core.*

class User: UnitNode(){
    val firstName by string()
    val lastName by string()
    val age by long()
    val friendsWith = relates(FriendsWith::class)
    val sharedPosts = relates(SharedPosts::class)
    val sharedPhotos = relates(SharedPhotos::class)
}
class FriendsWith: UnitRelation<User, User>(){
    val isFamily by boolean()
}
class SharedPosts: UnitRelation<User, Post>()
class SharedPhotos: UnitRelation<User, Photo>()

enum class MyEnum{
    A,
    B,
    C,
}

class Post: UnitNode(){
    val title by string()
    val content by string()
    val likes by string()
    val linkedPhoto = relates(LinkedPhoto::class)
}
class LinkedPhoto: UnitRelation<Post, Photo>()

class Photo: UnitNode(){
    val imageName by string()
    val likes by long()
    val tagged = relates(TaggedIn::class)
}
class TaggedIn: UnitRelation<Photo, User>()
