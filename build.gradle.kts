import com.jeanbarrossilva.orca.chrynan
import com.jeanbarrossilva.orca.loadable

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.android.maps.secrets) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.symbolProcessor) apply false
  alias(libs.plugins.moduleDependencyGraph)
  alias(libs.plugins.spotless)

  id(libs.plugins.orca.build.setup.formatting.get().pluginId)
  id(libs.plugins.orca.build.setup.java.get().pluginId)
  id("build-src")
}

allprojects { repositories.mavenCentral() }

subprojects subproject@{
  tasks.withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
      freeCompilerArgs.addAll("-Xstring-concat=inline")
    }
  }

  repositories {
    chrynan()
    google()
    gradlePluginPortal()
    loadable(project)
  }

  afterEvaluate {
    with(JavaVersion.toVersion(libs.versions.java.get())) java@{
      extensions.findByType<LibraryExtension>()?.apply {
        compileSdk = libs.versions.android.sdk.target.get().toInt()
        defaultConfig.minSdk = libs.versions.android.sdk.min.get().toInt()
        setDefaultNamespaceIfUnsetAndEligible(this@subproject)

        buildTypes { release { isMinifyEnabled = true } }

        compileOptions {
          sourceCompatibility = this@java
          targetCompatibility = this@java
        }
      }
    }
  }
}

tasks.named("clean") { delete(rootProject.buildDir) }
