plugins {
    alias(libs.plugins.kotlin.jvm)

    `java-library`
}

dependencies {
    implementation(libs.kotlin.reflect)

    testImplementation(project(":std:injector-test"))
    testImplementation(libs.assertk)
    testImplementation(libs.kotlin.test)
}
