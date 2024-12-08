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

package br.com.orcinus.orca.setup.formatting

import com.diffplug.gradle.spotless.SpotlessExtension
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.name
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

class FormattingSetupPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.extensions.getByType(SpotlessExtension::class.java).apply {
      cpp { extension ->
        extension
          .clangFormat("19.0.0")
          ?.pathToExe(
            Paths.get(BuildConfig.ANDROID_SDK_PATH)
              .resolve("ndk")
              .resolve(BuildConfig.NDK_VERSION)
              .resolve("toolchains")
              .resolve("llvm")
              .resolve("prebuilt")
              .let(Files::newDirectoryStream)
              .find { path -> path.name.startsWith(OperatingSystem.current().nativePrefix) }
              ?.resolve("bin")
              ?.resolve("clang-format")
              ?.toString()
          )
          ?.style(
            "file:" +
              Paths.get(".")
                .resolve("build-src")
                .resolve("setup")
                .resolve("formatting")
                .resolve("src")
                .resolve("main")
                .resolve("resources")
                .resolve(".clang-format")
          )
        extension.target("**\\/*.c")
      }
      java {
        it.googleJavaFormat()
        it.target("**\\/*.java")
      }
      kotlin {
        it.ktfmt().googleStyle()
        it.target("**\\/*.kt", "**\\/*.kts")
      }
    }
  }
}
