/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

import com.jeanbarrossilva.orca.Dimensions
import com.jeanbarrossilva.orca.namespace
import com.jeanbarrossilva.orca.namespaceFor

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  compileSdk = libs.versions.android.sdk.target.get().toInt()
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
  flavorDimensions += Dimensions.VERSION
  lint.disable += "Instantiatable"
  namespace = namespaceFor("app")

  buildFeatures {
    compose = true
    viewBinding = true
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFile("proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
  }

  defaultConfig {
    applicationId = project.namespace
    minSdk = libs.versions.android.sdk.min.get().toInt()
    targetSdk = libs.versions.android.sdk.target.get().toInt()
    versionCode = 2
    versionName = "0.1.1"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  productFlavors {
    register("default") { dimension = Dimensions.VERSION }

    register("demo") {
      applicationIdSuffix = ".demo"
      dimension = Dimensions.VERSION
      versionNameSuffix = "-demo"
    }
  }
}

dependencies {
  "androidTestDemoImplementation"(project(":core:sample-test"))
  "androidTestDemoImplementation"(project(":feature:gallery-test"))
  "androidTestDemoImplementation"(project(":platform:ui"))
  "androidTestDemoImplementation"(libs.android.activity.ktx)
  "androidTestDemoImplementation"(libs.android.compose.ui.test.junit)
  "androidTestDemoImplementation"(libs.assertk)

  androidTestImplementation(project(":platform:intents"))
  androidTestImplementation(project(":platform:ui-test"))
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.espresso.core)
  androidTestImplementation(libs.android.test.runner)

  "demoImplementation"(project(":core:sample"))

  ksp(project(":std:injector-processor"))

  implementation(project(":core:mastodon"))
  implementation(project(":core:shared-preferences"))
  implementation(project(":feature:composer"))
  implementation(project(":feature:feed"))
  implementation(project(":feature:gallery"))
  implementation(project(":feature:post-details"))
  implementation(project(":feature:profile-details"))
  implementation(project(":feature:search"))
  implementation(project(":feature:settings"))
  implementation(project(":feature:settings:term-muting"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:ui"))
  implementation(project(":std:injector"))
  implementation(libs.android.appcompat)
  implementation(libs.android.constraintlayout)
  implementation(libs.android.fragment.ktx)
  implementation(libs.android.material)
  implementation(libs.kotlin.reflect)
  implementation(libs.time4j)
}
