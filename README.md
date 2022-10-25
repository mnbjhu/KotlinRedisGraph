# Redis For Kotlin 
[![](https://jitpack.io/v/mnbjhu/KotlinRedisGraph.svg)](https://jitpack.io/#mnbjhu/KotlinRedisGraph)
## Overview
Inspired by Kotlin Exposed, this library aims to provide a type-safe DSL for interacting with Redis Graph to Kotlin.
#### Kotlin Redis Graph
* Construct schemas for nodes
* Create, read, update and delete nodes and relationships
* Perform path queries
## Setup
### Gradle
**Step 1.** Add the JitPack repository to your build.gradle
```groovy
allprojects {
    repositories {
        /* ... */
        maven { url "https://jitpack.io" }
    }
}
```
**Step 2.** Add the dependency
```groovy
dependencies {
  // Core DSL
    implementation "com.github.mnbjhu.KotlinRedisGraph:core:$kotlinRedisVersion"
  // Annotations
    kapt "com.github.mnbjhu.KotlinRedisGraph:annotations:$kotlinRedisVersion"
}
```
### Gradle Kotlin DSL
**Step 1.** Add the JitPack repository to your build.gradle.kts
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
```
**Step 2.** Add the dependency
```kotlin
dependencies {
  // Core DSL
  implementation("com.github.mnbjhu.KotlinRedisGraph:core:$kotlinRedisVersion")
  // Annotations
  implementation("com.github.mnbjhu.KotlinRedisGraph:annotations:$kotlinRedisVersion")
}
```
## Basic Usage
### Connect To Redis
To start using Redis Graph you need to create a new instance of the RedisGraph class with a graph name, host address and an option port (default is 6379).
```kotlin
import uk.gibby.redis.core.RedisGraph

val moviesGraph = RedisGraph(
    name = "movies",
    host = "raspberrypi.local",
)
```
### Define A Schema
Node types and their relationships are defined by a schema. To create a node type, create a class which:

```kotlin
import uk.gibby.redis.core.UnitNode
import uk.gibby.redis.core.UnitRelation

class ActorNode: UnitNode(){
    val name by string()
    val actorId by long()
    val actedIn = relates(ActedInRelation::class)
}
class MovieNode : UnitNode(){
    val title by string()
    val releaseYear by long()
    val movieId by long()
}
class ActedInRelation: UnitRelation<ActorNode, MovieNode>(){
    val role by string()
}
```

#### Experimental CodeGen
This will generate the same as the above but will instead create instances of RedisRelation&lt;ActedIn&gt;, RedisNode&lt;Movie &gt; and RedisNode&lt;Actor&gt; respectively (as appose to UnitNode). This will allow you to return the nodes them self as the defined data classes.
```kotlin
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.annotation.Node
import uk.gibby.redis.annotation.RelatesTo

@RedisType
data class ActedIn(val role: String)

@Node
data class Movie(val title: String, val releaseYear: Long)

@Node
@Relates(to = Movie::class, by = "actedIn", data = ActedIn::class)
data class Actor(val name: String)
```
Attributes can be defined on both **RedisClass** and **RedisRelation**. While in either scope, you'll have access to functions for setting the values of the attributes.

**Currently Supported Types Are:**

<ins>Primitives</ins>

| Type    | Function  |
|---------|-----------|
| String  | string()  |
| Long    | long()    |
| Double  | double()  |
| Boolean | boolean() |

<ins>Arrays</ins>
```kotlin
class MyNode: UnitNode(){
    /* ND arrays of any result type are supported */
    val myArray by array(string())
    val my2DArray by array(array(string()))
}
```
<ins>Structured Types</ins>
```kotlin
import ...
/* Defines a type which can be returned */
class Vector3Result : StructResult<Vector3>() {
    val x: DoubleResult by double()
    val y: DoubleResult by double()
    val z: DoubleResult by double()
    
    fun ResultScope.getResult(): Vector3 = Vector3(!x, !y, !z)
    
    fun ParamMap.setResult(value: Vector3){
        x[value.x]
        y[value.y]
        z[value.z]
    }
}
/* Defining the attribute will allow you to store the type on a node or relation */
class Vector3Attribute : Vector3Result(), Attribute<Vector3>

class MyNode: UnitNode(){
    val myVector by ::Vector3Attribute
}
```

#### Experimental CodeGen Annotation
```kotlin
@RedisType
data class Vector(val x: Double, val y: Double, val z: Double)
```
### Create Nodes
After a node type has been defined as a **RedisClass**, you can create a single instance like so:
```kotlin
moviesGraph.create(::MovieNode) {
    title["Star Wars: Episode V - The Empire Strikes Back"]
    releaseYear[1980]
    movieId[1]
}
```
##### Generated Cypher
```cypher
CREATE (:MovieNode{title:'Star Wars: Episode V - The Empire Strikes Back', release_year:1980, movie_id:1})
```
(If an attribute is defined in the type but not set on creation, and exception will be thrown)
  
You can also create multiple instances by mapping elements from a list.
```kotlin
var index = 1L
val actors = listOf(
    "Mark Hamill",
    "Harrison Ford",
    "Carrie Fisher"
)
moviesGraph.create(::ActorNode, actors) {
    name[it]
    actorId[index++]
}
```

##### Generated Cypher
```cypher
CREATE (:ActorNode{name:'Mark Hamill', actor_id:1}), (:ActorNode{name:'Harrison Ford', actor_id:2}), (:ActorNode{name:'Carrie Fisher', actor_id:3})
```
### Query Scope
Currently, all other functionality is performed with the **query** function which takes a lambda returning a result value.
#### Example:
Returns a list of all the movie names in the graph.
```kotlin
moviesGraph.query {
    match(::MovieNode)
    movie.title
}
```
### Create Relationships
```kotlin
moviesGraph.query {
    val (actor, movie) = match(::ActorNode, ::MovieNode)
    where ((actor.actorId eq 1) and (movie.movieId eq 1))
    create(actor - { actedIn { role["Luke Skywalker"] } } - movie)
}
moviesGraph.query {
    val (actor, movie) = match(::ActorNode, ::MovieNode)
    where ((actor.actorId eq 2) and (movie.movieId eq 1))
    create(actor - { actedIn{role["Han Solo"]} } - movie)
}
moviesGraph.query {
    val (actor, movie) = match(::ActorNode, ::MovieNode)
    where ( (actor.actorId eq 3) and (movie.movieId eq 1) )
    create(actor - { actedIn{role["Princess Leia"]} } - movie)
}
```
##### Generated Cypher
```cypher
MATCH (actor:ActorNode), (movie:MovieNode)
WHERE (actor.actor_id = 1) AND (movie.movie_id = 1)
CREATE (actor)-[r:ACTED_IN {role:'Luke Skywalker'}]->(movie)
```
Alternatively you can create the node and edges as a single query:
```kotlin
graph.query {
    val movie = create(::MovieNode {
        it[title] = "Star Wars: Episode V - The Empire Strikes Back"
        it[releaseYear] = 1980
    })
    create(::ActorNode{ it[name] = "Mark Hamill" } - { actedIn { it[role] = "Luke Skywalker" } } - movie)
    create(::ActorNode{ it[name] = "Harrison Ford" } - { actedIn{ it[role] = "Han Solo" } } - movie)
    create(::ActorNode{ it[name] = "Carrie Fisher" } - { actedIn{ it[role] = "Princess Leia" } } - movie)
}
```
### Constructing Queries
In this example we search for all movies and return the movie 'title'.

The same however we also return the 'releaseYear' and the 'movieId':
```kotlin
val movies = moviesGraph.query{
    val movie = match(::MovieNode)
    movie.title
}
movies `should contain` "Star Wars: Episode V - The Empire Strikes Back"
```
##### Generated Cypher
```cypher
MATCH (movie:MovieNode)
RETURN movie.title
```

When using the @Node annotation we can return the movie node itself
```kotlin
@Node
data class Movie(val title: String, val releaseYear: Long, val movieId: Long)

val (title, releaseYear, id) = moviesGraph.query {
    val movie = match(::MovieNode)
    movie
}.first()

title as String `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"
releaseYear as Long `should be equal to` 1980
id as Long `should be equal to` 1
```
##### Generated Cypher
```cypher
MATCH (movie:MovieNode)
RETURN movie.title, movie.release_year, movie.movie_id
```
Here we:
* Search for an actor and a movie where the actor acted in the movie.
* Filter by movieId = 1
* And return the actor name and movie title
```kotlin
val actedInMovies = moviesGraph.query {
    val (actor, _, movie) = match(::ActorNode - { actedIn }  - ::MovieNode)
    where (movie.movieId eq 1)
    orderBy(actor.actorId)
    result(actor.name, movie.title)
}

actedInMovies.size `should be equal to` 3

val (actorName, movieName) = actedInMovieNodes.last()

actorName `should be equal to` "Carrie Fisher"
movieName `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"
```
##### Generated Cypher
```cypher
MATCH (actor:ActorNode)-[movieRelation:ACTED_IN]-(movie:MovieNode)
WHERE movie.movie_id = 1
RETURN actor.name, movie.title
ORDER BY actor.actor_id
```
### Delete Nodes And Relationships
Any nodes or relationships referenced in the Query block can be deleted calling them in the (vararg) delete function:
```kotlin
val removedActor = moviesGraph.query {
    val (actor, relationship) = match(::ActorNode - { actedIn }  - ::MovieNode)
    where (actor.actorId eq 1)
    delete(relationship)
}
```
##### Generated Cypher
```cypher
MATCH (actor:ActorNode)-[movieRelation:ACTED_IN]-(movie:MovieNode)
WHERE actor.actor_id = 1
RETURN movieRelation.role
```
