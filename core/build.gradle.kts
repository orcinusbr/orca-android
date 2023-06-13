plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
}

dependencies {
    api(Dependencies.COROUTINES_CORE)
}
