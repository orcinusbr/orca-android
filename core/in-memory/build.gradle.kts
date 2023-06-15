plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
}

dependencies {
    api(project(":core"))

    testImplementation(Dependencies.COROUTINES_TEST)
    testImplementation(kotlin("test"))
}
