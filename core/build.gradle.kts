import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Versions.java.toString()
    }
}

dependencies {
    api(Dependencies.COROUTINES_CORE)

    testImplementation(project(":core:sample"))
    testImplementation(project(":core-test"))
    testImplementation(kotlin("test"))
    testImplementation(Dependencies.COROUTINES_TEST)
}
