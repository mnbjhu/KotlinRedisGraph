
group = "uk.gibby"
version = "0.3.3"


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
        "implementation"(kotlin("stdlib", version = "1.7.10"))
        "implementation"(kotlin("reflect", version = "1.7.10"))
    }
}




