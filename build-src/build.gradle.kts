
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins.register(name) {
        id = name
        implementationClass = "com.jeanbarrossilva.orca.plugin.BuildSrcPlugin"
    }
}

withJavaVersionString {
    tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(this@withJavaVersionString))

        java {
            sourceCompatibility = JavaVersion.toVersion(this@withJavaVersionString)
            targetCompatibility = JavaVersion.toVersion(this@withJavaVersionString)
        }
    }
}

/**
 * Extracts the Java version [String] from the `libs.versions.toml` [File] and runs the [action]
 * with it.
 *
 * @param action Operation to be performed with the extracted version.
 **/
fun withJavaVersionString(action: String.() -> Unit) {
    rootDir
        .parentFile
        .listFiles { file, name -> file.isDirectory && name == "gradle" }
        ?.first()
        ?.listFiles { _, name -> name == "libs.versions.toml" }
        ?.first()
        ?.bufferedReader()
        ?.useLines { lines ->
            lines
                .map(String::trim)
                .dropUntil { it.replace(" ", "") == "[versions]" }
                .first { it.split(' ').firstOrNull()?.startsWith("java") == true }
                .split('=')
                .last()
                .replace('"', ' ')
                .trim()
                .run(action)
        }
}

/**
 * Drops all elements until the [predicate] is satisfied.
 *
 * @param predicate Condition to be met for a given element to not be dropped.
 **/
fun <T> Sequence<T>.dropUntil(predicate: (T) -> Boolean): Sequence<T> {
    var toDrop = 0
    filter { element ->
        predicate(element).also { isMatch ->
            if (!isMatch) {
                ++toDrop
            }
        }
    }
    return drop(toDrop)
}
