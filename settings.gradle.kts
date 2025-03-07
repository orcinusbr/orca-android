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

rootProject.name = "Orca"

pluginManagement.repositories {
  google()
  gradlePluginPortal()
  mavenCentral()
}

includeBuild("build-src")

include(
  ":app",
  ":composite:composable",
  ":composite:status",
  ":composite:timeline",
  ":composite:timeline-test",
  ":core",
  ":core:mastodon",
  ":core:sample",
  ":core:sample-test",
  ":core:shared-preferences",
  ":core-module",
  ":core-test",
  ":ext:coroutines",
  ":ext:grammar",
  ":ext:processing",
  ":ext:reflection",
  ":ext:testing",
  ":ext:uri",
  ":feature:composer",
  ":feature:feed",
  ":feature:feed-test",
  ":feature:gallery",
  ":feature:gallery-test",
  ":feature:post-details",
  ":feature:profile-details",
  ":feature:profile-details-test",
  ":feature:registration",
  ":feature:registration:credentials",
  ":feature:registration:ongoing",
  ":feature:search",
  ":feature:settings",
  ":feature:settings:term-muting",
  ":platform:animator",
  ":platform:autos",
  ":platform:cache",
  ":platform:autos-test",
  ":platform:core",
  ":platform:focus",
  ":platform:ime",
  ":platform:ime-test",
  ":platform:intents",
  ":platform:intents-test",
  ":platform:navigation",
  ":platform:navigation-test",
  ":platform:stack",
  ":platform:starter",
  ":platform:starter:lifecycle",
  ":platform:testing",
  ":platform:testing:compose",
  ":std:func",
  ":std:func-test",
  ":std:image",
  ":std:image:android",
  ":std:image:compose",
  ":std:image-test",
  ":std:injector",
  ":std:injector-processor",
  ":std:injector-test",
  ":std:markdown",
  ":std:visibility",
  ":std:visibility-check"
)
