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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.form

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.form.item.BinaryChannelItemKSerializer
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.form.item.BinaryItemKSerializer
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.form.item.FileItemKSerializer
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.form.item.FormItemKSerializer
import io.ktor.http.HeaderValueParam
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.streams.asInput
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class PartDataKSerializerTests {
  private val input = byteArrayOf(0).inputStream().asInput()

  @AfterTest
  fun tearDown() {
    input.close()
  }

  @Test
  fun serializesBinaryChannelItem() {
    val binaryChannelItem =
      PartData.BinaryChannelItem(
        { byteArrayOf(0).inputStream().toByteReadChannel() },
        Headers.Empty
      )

    @OptIn(ExperimentalSerializationApi::class)
    assertThat(Json.encodeToString(PartDataKSerializer, binaryChannelItem))
      .isEqualTo(
        buildJsonObject {
            put(
              PartDataKSerializer.descriptor.getElementName(0),
              PartData.BinaryChannelItem::class.qualifiedName
            )
          }
          .toString() +
          Json.encodeToJsonElement(BinaryChannelItemKSerializer, binaryChannelItem).toString()
      )
  }

  @Test
  fun serializesBinaryItem() {
    val binaryItem = PartData.BinaryItem({ input }, dispose = input::close, Headers.Empty)

    @OptIn(ExperimentalSerializationApi::class)
    assertThat(Json.encodeToString(PartDataKSerializer, binaryItem))
      .isEqualTo(
        buildJsonObject {
            put(
              PartDataKSerializer.descriptor.getElementName(0),
              PartData.BinaryItem::class.qualifiedName
            )
          }
          .toString() + Json.encodeToJsonElement(BinaryItemKSerializer, binaryItem).toString()
      )
  }

  @Test
  fun serializesFileItem() {
    val fileItem = PartData.FileItem({ input }, dispose = input::close, Headers.Empty)

    @OptIn(ExperimentalSerializationApi::class)
    assertThat(Json.encodeToString(PartDataKSerializer, fileItem))
      .isEqualTo(
        buildJsonObject {
            put(
              PartDataKSerializer.descriptor.getElementName(0),
              PartData.FileItem::class.qualifiedName
            )
          }
          .toString() + Json.encodeToJsonElement(FileItemKSerializer, fileItem).toString()
      )
  }

  @Test
  fun serializesFormItem() {
    val formItem =
      PartData.FormItem("${HeaderValueParam("filename", "file.png")}", dispose = {}, Headers.Empty)

    @OptIn(ExperimentalSerializationApi::class)
    assertThat(Json.encodeToString(PartDataKSerializer, formItem))
      .isEqualTo(
        buildJsonObject {
            put(
              PartDataKSerializer.descriptor.getElementName(0),
              PartData.FormItem::class.qualifiedName
            )
          }
          .toString() + Json.encodeToJsonElement(FormItemKSerializer, formItem).toString()
      )
  }
}
