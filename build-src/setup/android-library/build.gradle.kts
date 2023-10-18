plugins {
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.jvm)

  `java-gradle-plugin`
}

buildConfig {
  packageName("com.jeanbarrossilva.orca.build.setup.android.library")
  buildConfigField("String", "JAVA_VERSION", "\"${libs.versions.java.get()}\"")
  buildConfigField("String", "MIN_SDK_VERSION", "\"${libs.versions.android.sdk.min.get()}\"")
  buildConfigField("String", "TARGET_SDK_VERSION", "\"${libs.versions.android.sdk.target.get()}\"")
}

dependencies { implementation(libs.android.plugin) }

gradlePlugin.plugins.register("setup-android-library") {
  id = libs.plugins.orca.build.setup.android.library.get().pluginId
  implementationClass =
    "com.jeanbarrossilva.orca.build.setup.android.library.AndroidLibrarySetupPlugin"
}

repositories.google()
