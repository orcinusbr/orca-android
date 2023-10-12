import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/** Key of this TOML-"`key = value`"-formatted [String]. * */
private val String.tomlKey
  get() = replace(" ", "").split('=').first()

/** Value of this TOML-"`key = value`"-formatted [String]. * */
private val String.tomlValue
  get() = split('=').last().replace('"', ' ').trim()

plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
}

repositories { mavenCentral() }

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
 */
fun withJavaVersionString(action: String.() -> Unit) {
  rootDir.parentFile
    .listFiles { file, name -> file.isDirectory && name == "gradle" }
    ?.first()
    ?.listFiles { _, name -> name == "libs.versions.toml" }
    ?.first()
    ?.reader()
    ?.useLines { lines ->
      lines
        .dropUntil { it.isTomlTableHeader("versions") }
        .first { it.tomlKey == "java" }
        .tomlValue
        .run(action)
    }
}

/**
 * Drops all elements until the [predicate] is satisfied.
 *
 * @param predicate Condition to be met for a given element to not be dropped.
 */
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

/**
 * Returns whether this [String] is a [TOML table header](https://toml.io/en/v1.0.0#table).
 *
 * @param tableName Name of the table to be checked if it's defined in the header.
 */
fun String.isTomlTableHeader(tableName: String): Boolean {
  return with(trim()) {
    startsWith('[') && endsWith(']') && substringAfter('[').substringBefore(']').trim() == tableName
  }
}
