import java.util.Properties

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

plugins {
  alias(libs.plugins.buildconfig)
  alias(libs.plugins.kotlin.jvm)

  `java-gradle-plugin`
}

buildConfig {
  val androidSdk =
    Properties()
      .apply { load(rootProject.file("../local.properties").reader()) }
      .getProperty("sdk.dir")
  buildConfigField("String", "ANDROID_SDK_PATH", "\"$androidSdk\"")
  buildConfigField("String", "NDK_VERSION", "\"${libs.versions.android.ndk.get()}\"")
  packageName("br.com.orcinus.orca.setup.formatting")
}

dependencies { implementation(libs.spotless) }

gradlePlugin.plugins.register("setup-formatting") {
  id = libs.plugins.orca.setup.formatting.get().pluginId
  implementationClass = "br.com.orcinus.orca.setup.formatting.FormattingSetupPlugin"
}
