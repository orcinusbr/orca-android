/*
 * Copyright © 2023–2025 Orcinus
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

import kotlin.io.path.listDirectoryEntries
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.tomlj.Toml

plugins {
  alias(libs.plugins.kotlin.jvm) apply false

  `java-gradle-plugin`
  `kotlin-dsl`
}

allprojects {
  repositories.mavenCentral()

  rootDir.parentFile
    .toPath()
    .listDirectoryEntries("gradle")
    .getOrNull(0)
    ?.resolve("libs.versions.toml")
    ?.let(Toml::parse)
    ?.getTable("versions")
    ?.getString("java")
    ?.let { javaVersionAsString ->
      tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(javaVersionAsString))

        java {
          val version = JavaVersion.toVersion(javaVersionAsString)
          sourceCompatibility = version
          targetCompatibility = version
        }
      }
    }
}

buildscript { dependencies.classpath(libs.tomlJ) }

gradlePlugin {
  plugins.register(name) {
    id = name
    implementationClass = "br.com.orcinus.orca.plugin.BuildSrcPlugin"
  }
}
