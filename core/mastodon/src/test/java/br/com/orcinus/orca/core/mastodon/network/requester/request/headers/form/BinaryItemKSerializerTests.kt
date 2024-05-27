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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.form

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.input.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.strings.serializer
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.streams.asInput
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject

internal class BinaryItemKSerializerTests {
  @Test
  fun serializes() {
    assertThat(
        Json.encodeToString(
          BinaryItemKSerializer,
          PartData.BinaryItem(
            byteArrayOf(0).inputStream().asInput().use { { it } },
            dispose = {},
            Headers.Empty
          )
        )
      )
      .isEqualTo(
        @OptIn(ExperimentalSerializationApi::class)
        buildJsonObject {
            put(
              BinaryItemKSerializer.descriptor.getElementName(0),
              byteArrayOf(0).inputStream().asInput().use {
                Json.encodeToJsonElement(Input.serializer(), it)
              }
            )
            put(
              BinaryItemKSerializer.descriptor.getElementName(1),
              Json.encodeToJsonElement(StringValues.serializer(), Headers.Empty)
            )
          }
          .toString()
      )
  }

  @Test
  fun deserializes() {
    assertThat(
        Json.decodeFromString(
          BinaryItemKSerializer,
          @OptIn(ExperimentalSerializationApi::class)
          buildJsonObject {
              put(
                BinaryItemKSerializer.descriptor.getElementName(0),
                byteArrayOf(0).inputStream().asInput().use {
                  Json.encodeToJsonElement(Input.serializer(), it)
                }
              )
              put(
                BinaryItemKSerializer.descriptor.getElementName(1),
                Json.encodeToJsonElement(StringValues.serializer(), Headers.Empty)
              )
            }
            .toString()
        )
      )
      .all {
        transform("binary") { it.provider() }
          .hasSameContentAs(byteArrayOf(0).inputStream().asInput())
        transform("headers", PartData.BinaryItem::headers).isEqualTo(Headers.Empty)
      }
  }
}
