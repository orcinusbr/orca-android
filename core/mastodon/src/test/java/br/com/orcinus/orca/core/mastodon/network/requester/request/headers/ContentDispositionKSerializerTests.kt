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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.http.ContentDisposition
import io.ktor.http.HeaderValueParam
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

internal class ContentDispositionKSerializerTests {
  @Test
  fun serializes() {
    assertThat(
        Json.encodeToString(
          ContentDisposition.serializer(),
          ContentDisposition(
            ContentDisposition.Attachment.disposition,
            listOf(HeaderValueParam("filename", "file.png"))
          )
        )
      )
      .isEqualTo(
        @OptIn(ExperimentalSerializationApi::class)
        buildJsonObject {
            put(
              ContentDisposition.serializer().descriptor.getElementName(0),
              ContentDisposition.Attachment.disposition
            )
            putJsonArray(ContentDisposition.serializer().descriptor.getElementName(1)) {
              add(
                Json.encodeToJsonElement(
                  HeaderValueParamKSerializer,
                  HeaderValueParam("filename", "file.png")
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
          ContentDisposition.serializer(),
          @OptIn(ExperimentalSerializationApi::class)
          buildJsonObject {
              put(
                ContentDisposition.serializer().descriptor.getElementName(0),
                ContentDisposition.Attachment.disposition
              )
              putJsonArray(ContentDisposition.serializer().descriptor.getElementName(1)) {
                add(
                  Json.encodeToJsonElement(
                    HeaderValueParamKSerializer,
                    HeaderValueParam("filename", "file.png")
                  )
                )
              }
            }
            .toString()
        )
      )
      .isEqualTo(
        ContentDisposition(
          ContentDisposition.Attachment.disposition,
          listOf(HeaderValueParam("filename", "file.png"))
        )
      )
  }
}
