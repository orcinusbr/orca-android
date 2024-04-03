/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

import br.com.orcinus.orca.Dimensions
import br.com.orcinus.orca.namespaceFor

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
  testOptions.unitTests.isIncludeAndroidResources = true

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
    applicationId = "com.jeanbarrossilva.orca"
    minSdk = libs.versions.android.sdk.min.get().toInt()
    targetSdk = libs.versions.android.sdk.target.get().toInt()
    versionCode = 4
    versionName = "v0.3"
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
  "androidTestDemoImplementation"(project(":composite:timeline"))
  "androidTestDemoImplementation"(project(":feature:gallery-test"))
  "androidTestDemoImplementation"(project(":platform:intents-test"))
  "androidTestDemoImplementation"(project(":platform:navigation-test"))
  "androidTestDemoImplementation"(project(":platform:testing"))
  "androidTestDemoImplementation"(project(":platform:testing:compose"))
  "androidTestDemoImplementation"(libs.assertk)
  "androidTestDemoImplementation"(libs.android.compose.ui.test.manifest)

  androidTestImplementation(project(":composite:timeline-test"))
  androidTestImplementation(project(":platform:intents-test"))
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.runner)

  "demoImplementation"(project(":platform:core"))

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
  implementation(project(":platform:core"))
  implementation(project(":platform:intents"))
  implementation(project(":platform:navigation"))
  implementation(project(":std:image:compose"))
  implementation(project(":std:injector"))
  implementation(libs.android.appcompat)
  implementation(libs.android.constraintlayout)
  implementation(libs.android.fragment.ktx)
  implementation(libs.android.material)

  releaseImplementation(libs.kotlin.reflect)

  "testDemoImplementation"(project(":composite:timeline"))
  "testDemoImplementation"(project(":composite:timeline-test"))
  "testDemoImplementation"(project(":core:sample-test"))
  "testDemoImplementation"(project(":feature:feed-test"))
  "testDemoImplementation"(project(":feature:gallery-test"))
  "testDemoImplementation"(project(":platform:intents-test"))
  "testDemoImplementation"(project(":platform:navigation-test"))
  "testDemoImplementation"(project(":platform:testing"))
  "testDemoImplementation"(libs.android.test.espresso.core)
  "testDemoImplementation"(libs.assertk)
  "testDemoImplementation"(libs.kotlin.test)
  "testDemoImplementation"(libs.openTest4J)
  "testDemoImplementation"(libs.robolectric)
}

kotlin.compilerOptions.freeCompilerArgs.addAll("-Xcontext-receivers")
