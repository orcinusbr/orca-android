/*
 * Copyright © 2023-2024 Orca
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

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.android.maps.secrets)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

  buildFeatures {
    buildConfig = true
    compose = true
  }

  secrets {
    defaultPropertiesFileName = "public.properties"
    ignoreList += "^(?!mastodon\\.clientSecret).*$"
  }

  packagingOptions.resources.excludes +=
    arrayOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
}

dependencies {
  androidTestImplementation(project(":platform:autos-test"))
  androidTestImplementation(project(":platform:intents-test"))
  androidTestImplementation(project(":platform:testing"))
  androidTestImplementation(project(":std:injector-test"))
  androidTestImplementation(libs.android.compose.ui.test.junit)
  androidTestImplementation(libs.android.test.core)
  androidTestImplementation(libs.android.test.runner)
  androidTestImplementation(libs.mockk)

  api(project(":core-module"))
  api(project(":composite:composable"))

  ksp(project(":std:injector-processor"))

  implementation(project(":composite:timeline"))
  implementation(project(":core:sample"))
  implementation(project(":ext:coroutines"))
  implementation(project(":platform:autos"))
  implementation(project(":platform:cache"))
  implementation(project(":platform:focus"))
  implementation(project(":platform:navigation"))
  implementation(project(":platform:starter"))
  implementation(libs.android.browser)
  implementation(libs.android.room.ktx)
  implementation(libs.kotlin.reflect)
  implementation(libs.ktor.client.cio)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.contentNegotiation)
  implementation(libs.ktor.serialization.json)
  implementation(libs.loadable)
  implementation(libs.paginate)

  ksp(libs.android.room.compiler)

  releaseImplementation(libs.slf4j) {
    because("Ktor references \"StaticLoggerBinder\" and it is missing on minification.")
  }

  testImplementation(project(":core:sample"))
  testImplementation(project(":core:sample-test"))
  testImplementation(project(":core-test"))
  testImplementation(libs.assertk)
  testImplementation(libs.junit)
  testImplementation(libs.kotlin.coroutines.test)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.ktor.client.mock)
  testImplementation(libs.mockk)
  testImplementation(libs.turbine)
}
