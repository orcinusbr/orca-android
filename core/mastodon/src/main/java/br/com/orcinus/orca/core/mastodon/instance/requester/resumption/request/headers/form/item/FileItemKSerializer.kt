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

import br.com.orcinus.orca.core.mastodon.instance.requester.InternalRequesterApi
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.serializer
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.strings.serializer
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.strings.toHeaders
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import io.ktor.utils.io.core.Input
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/** [KSerializer] for serializing and deserializing a [PartData.FileItem]. */
@InternalRequesterApi
internal object FileItemKSerializer : KSerializer<PartData.FileItem> {
  override val descriptor =
    buildClassSerialDescriptor(FileItemKSerializer::class.java.name) {
      element("file", Input.serializer().descriptor)
      element("headers", StringValues.serializer().descriptor)
    }

  override fun serialize(encoder: Encoder, value: PartData.FileItem) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(descriptor, index = 0, Input.serializer(), value.provider())
      encodeSerializableElement(descriptor, index = 1, StringValues.serializer(), value.headers)
    }
  }

  override fun deserialize(decoder: Decoder): PartData.FileItem {
    return decoder.decodeStructure(descriptor) {
      lateinit var file: Input
      lateinit var headers: Headers
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> file = decodeSerializableElement(descriptor, index, Input.serializer())
          1 ->
            headers =
              decodeSerializableElement(descriptor, index, StringValues.serializer()).toHeaders()
          else -> break
        }
      }
      PartData.FileItem({ file }, dispose = file::close, headers)
    }
  }
}
