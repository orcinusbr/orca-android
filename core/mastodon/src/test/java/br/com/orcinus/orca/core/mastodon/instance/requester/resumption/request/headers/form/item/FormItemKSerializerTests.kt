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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.form.item

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.strings.serializer
import io.ktor.http.HeaderValueParam
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class FormItemKSerializerTests {
  @Test
  fun serializes() {
    assertThat(
        Json.encodeToString(
          FormItemKSerializer,
          PartData.FormItem(
            "${HeaderValueParam("filename", "file.png")}",
            dispose = {},
            Headers.Empty
          )
        )
      )
      .isEqualTo(
        @OptIn(ExperimentalSerializationApi::class)
        buildJsonObject {
            put(
              FormItemKSerializer.descriptor.getElementName(0),
              "${HeaderValueParam("filename", "file.png")}"
            )
            put(
              FormItemKSerializer.descriptor.getElementName(1),
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
          FormItemKSerializer,
          @OptIn(ExperimentalSerializationApi::class)
          buildJsonObject {
              put(
                FormItemKSerializer.descriptor.getElementName(0),
                "${HeaderValueParam("filename", "file.png")}"
              )
              put(
                FormItemKSerializer.descriptor.getElementName(1),
                Json.encodeToJsonElement(StringValues.serializer(), Headers.Empty)
              )
            }
            .toString()
        )
      )
      .all {
        transform("value", PartData.FormItem::value)
          .isEqualTo("${HeaderValueParam("filename", "file.png")}")
        transform("headers", PartData.FormItem::headers).isEqualTo(Headers.Empty)
      }
  }
}
