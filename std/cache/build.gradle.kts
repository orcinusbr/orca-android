plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
}

dependencies {
    implementation(Dependencies.COROUTINES_CORE)

    testImplementation(kotlin("test"))
    testImplementation(Dependencies.COROUTINES_TEST)
    testImplementation(Dependencies.MOCKITO)
}
