
group = "uk.gibby"
version = "0.8.8"


buildscript {
    repositories {
        mavenCentral()
    }
    
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    repositories {
        mavenCentral()
    }

    dependencies {
        "api"(kotlin("stdlib", version = "1.7.10"))
        "api"(kotlin("reflect", version = "1.7.10"))
    }
}




