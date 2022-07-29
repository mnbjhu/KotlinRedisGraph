# Redis For Kotlin [![](https://jitpack.io/v/mnbjhu/KotlinRedisGraph.svg)](https://jitpack.io/#mnbjhu/KotlinRedisGraph)
## Overview
Inspired by Kotlin Exposed, this library aims to provide a type-safe Kotlin DSL for interacting with Redis databases.
#### Redis Graph
* Construct schemas for nodes
* Create and delete nodes
* Create and delete relationships between nodes
* Perform path queries
## Setup
### Gradle
**Step 1.** Add the JitPack repository to your build.gradle
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
**Step 2.** Add the dependency
```groovy
dependencies {
    implementation "com.github.mnbjhu:KotlinRedisGraph:$kotlinRedisVersion"
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
    implementation("com.github.mnbjhu:KotlinRedisGraph:$kotlinRedisVersion")
}
```
## Basic Usage
### Connect To Redis
To start using Redis Graph you need to create a new instance of the RedisGraph class with a graph name, host address and an option port (default is 6379).
```kotlin
import api.RedisGraph

val moviesGraph = RedisGraph(
    name = "movies",
    host = "raspberrypi.local",
)
```
### Define A Schema
Node types and their relationships are defined by a schema. To create a node type, create a class which:
* Extends **RedisClass**
* Overrides **instanceName** as a single constructor parameter
* Sets the **typeName** in the **RedisClass** constructor

Attributes can be defined on both **RedisClass** and **RedisRelation**. While in either scope, you'll have access to functions for creating instances of **Attribute<T>**.
  
**Current Supported Types Are:**
  Type | Function
  --- | ---
  String | string()
  Long | int()
  Double | double()
  Boolean | boolean()
```kotlin
import api.RedisClass
import api.RedisRelation

class Actor(override val instanceName: String) : RedisClass("Actor"){
    val name = string("name")
    val actorId = int("actor_id")
    val actedIn = relates(ActedIn::class)
}
class Movie(override val instanceName: String) : RedisClass("Movie"){
    val title = string("title")
    val releaseYear = int("release_year")
    val movieId = int("movie_id")
}
class ActedIn(from: Actor, to: Movie, override val instanceName: String):
    RedisRelation<Actor, Movie>(from, to, "ACTED_IN"){
    val role = string("role")
}
```
### Create Nodes
After a node type has been defined as a **RedisClass**, you can create a single instance like so:
```kotlin
import schemas.Actor
import schemas.Movie

moviesGraph.create(Movie::class) {
    title["Star Wars: Episode V - The Empire Strikes Back"]
    releaseYear[1980]
    movieId[1]
}
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
moviesGraph.create(Actor::class, actors) {
    name[it]
    actorId[index++]
}
```
### Create Relationships
```kotlin
moviesGraph.query {
    val actor = variableOf<Actor>("actor")
    val movie = variableOf<Movie>("movie")
    where {
        (actor.actorId eq 1) and (movie.movieId eq 1)
    }
    create {
        val actedIn = actor.actedIn("r") { role["Luke Skywalker"] } - movie
        result(actedIn.role) { actedIn.role() }
    }
}
```
### Make Queries
```kotlin
val movies = moviesGraph.query {
    val movie = variableOf<Movie>("movie")
    result(movie.title){movie.title()}
}
movies `should contain` "Star Wars: Episode V - The Empire Strikes Back"
```
```kotlin
val (title, releaseYear, id) = moviesGraph.query {
    val movie = variableOf<Movie>("movie")
    result(movie.title, movie.releaseYear, movie.movieId){
        Triple(movie.title(), movie.releaseYear(), movie.movieId())
    }
}.first()

title `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"
releaseYear `should be equal to` 1980
id `should be equal to` 1
```
```kotlin
val actedIn = moviesGraph.query {
    val actor = variableOf<Actor>("actor")
    val (movie) = actor.actedIn("relationship")
    where {
        movie.movieId eq 1
    }
    result(actor.name, movie.title){
        actor.name() to movie.title()
    }
}

actedIn.size `should be equal to` 3

val (actorName, movieName) = actedIn.last()

actorName `should be equal to` "Carrie Fisher"
movieName `should be equal to` "Star Wars: Episode V - The Empire Strikes Back"
```

## Documentation
## Plannd Features
