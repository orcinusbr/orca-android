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

package br.com.orcinus.orca.core.mastodon.network.request.headers

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.http.ContentType
import io.ktor.http.HeaderValueParam
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

internal class ContentTypeKSerializerTests {
  @Test
  fun serializes() {
    assertThat(
        Json.encodeToString(
          ContentType.serializer(),
          ContentType("text", "html", listOf(HeaderValueParam("charset", "utf-8")))
        )
      )
      .isEqualTo(
        @OptIn(ExperimentalSerializationApi::class)
        buildJsonObject {
            put(ContentType.serializer().descriptor.getElementName(0), "text")
            put(ContentType.serializer().descriptor.getElementName(1), "html")
            putJsonArray(ContentType.serializer().descriptor.getElementName(2)) {
              add(
                Json.encodeToJsonElement(
                  HeaderValueParamKSerializer,
                  HeaderValueParam("charset", "utf-8")
                )
              )
            }
          }
          .toString()
      )
  }

  @Test
  fun deserializes() {
    assertThat(
        Json.decodeFromString(
          ContentType.serializer(),
          @OptIn(ExperimentalSerializationApi::class)
          buildJsonObject {
              put(ContentType.serializer().descriptor.getElementName(0), "text")
              put(ContentType.serializer().descriptor.getElementName(1), "html")
              putJsonArray(ContentType.serializer().descriptor.getElementName(2)) {
                add(
                  Json.encodeToJsonElement(
                    HeaderValueParamKSerializer,
                    HeaderValueParam("charset", "utf-8")
                  )
                )
              }
            }
            .toString()
        )
      )
      .isEqualTo(ContentType("text", "html", listOf(HeaderValueParam("charset", "utf-8"))))
  }
}
