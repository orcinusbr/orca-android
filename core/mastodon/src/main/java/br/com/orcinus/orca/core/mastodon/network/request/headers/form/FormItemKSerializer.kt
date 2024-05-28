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

package br.com.orcinus.orca.core.mastodon.network.request.headers.form

import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.serializer
import br.com.orcinus.orca.core.mastodon.network.request.headers.strings.toHeaders
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/** [KSerializer] for serializing and deserializing a [PartData.FormItem]. */
@InternalNetworkApi
internal object FormItemKSerializer : KSerializer<PartData.FormItem> {
  override val descriptor =
    buildClassSerialDescriptor(FormItemKSerializer::class.java.name) {
      element<String>("value")
      element("headers", StringValues.serializer().descriptor)
    }

  override fun serialize(encoder: Encoder, value: PartData.FormItem) {
    encoder.encodeStructure(descriptor) {
      encodeStringElement(descriptor, index = 0, value.value)
      encodeSerializableElement(descriptor, index = 1, StringValues.serializer(), value.headers)
    }
  }

  override fun deserialize(decoder: Decoder): PartData.FormItem {
    return decoder.decodeStructure(descriptor) {
      lateinit var value: String
      lateinit var headers: Headers
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> value = decodeStringElement(descriptor, index)
          1 ->
            headers =
              decodeSerializableElement(descriptor, index, StringValues.serializer()).toHeaders()
          else -> break
        }
      }
      PartData.FormItem(value, dispose = {}, headers)
    }
  }
}
