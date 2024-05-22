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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.strings

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.http.parametersOf
import io.ktor.util.StringValues
import kotlin.test.Test
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class StringValuesKSerializerTests {
  @Test
  fun serializes() {
    assertThat(
        Json.encodeToString(
          StringValues.serializer(),
          parametersOf("key0" to listOf("value0", "value1"), "key1" to listOf("value2"))
        )
      )
      .isEqualTo(
        Json.encodeToString(
          setOf(
            object : Map.Entry<String, List<String>> {
              override val key = "key0"
              override val value = listOf("value0", "value1")
            },
            object : Map.Entry<String, List<String>> {
              override val key = "key1"
              override val value = listOf("value2")
            }
          )
        )
      )
  }

  @Test
  fun deserializes() {
    assertThat(
        Json.decodeFromString(
          StringValues.serializer(),
          Json.encodeToString(
            StringValues.serializer(),
            parametersOf("key0" to listOf("value0", "value1"), "key1" to listOf("value2"))
          )
        )
      )
      .isEqualTo(
        StringValues.build {
          appendAll("key0", listOf("value0", "value1"))
          append("key1", "value2")
        }
      )
  }
}
