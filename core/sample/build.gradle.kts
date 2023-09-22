plugins {
    alias(libs.plugins.kotlin.jvm)

    `java-library`
}

dependencies {
    api(project(":core"))

    implementation(project(":core-test"))

    testImplementation(project(":core:sample-test"))
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.test)
}
