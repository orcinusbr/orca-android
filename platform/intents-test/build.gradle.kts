import br.com.orcinus.orca.namespaceFor

/*
 * Copyright © 2024 Orcinus
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
  alias(libs.plugins.kotlin.android)
}

android {
  defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  namespace = namespaceFor("platform.intents.test")
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  debugImplementation(libs.android.lifecycle.runtime)

  implementation(project(":ext:uri"))
  implementation(libs.android.test.espresso.intents)

  testImplementation(project(":platform:starter"))
  testImplementation(project(":platform:testing"))
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
}
