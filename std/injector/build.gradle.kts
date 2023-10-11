plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.symbolProcessor)

    `java-library`
}

dependencies {
    implementation(libs.kotlin.reflect)

    testImplementation(project(":std:injector-test"))
    testImplementation(libs.assertk)
    testImplementation(libs.kotlin.test)
}
