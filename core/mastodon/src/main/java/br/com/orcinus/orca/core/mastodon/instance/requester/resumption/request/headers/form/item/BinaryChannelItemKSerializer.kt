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
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.memory.serializer
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.strings.serializer
import br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.strings.toHeaders
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.util.StringValues
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * [KSerializer] for serializing and deserializing a binary channel item.
 *
 * @see PartData.BinaryChannelItem
 */
@InternalRequesterApi
internal object BinaryChannelItemKSerializer : KSerializer<PartData.BinaryChannelItem> {
  override val descriptor =
    buildClassSerialDescriptor(BinaryChannelItemKSerializer::class.java.name) {
      element("binaryChannel", ByteReadChannel.serializer().descriptor)
      element("headers", StringValues.serializer().descriptor)
    }

  override fun serialize(encoder: Encoder, value: PartData.BinaryChannelItem) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(
        descriptor,
        index = 0,
        ByteReadChannel.serializer(),
        value.provider()
      )
      encodeSerializableElement(descriptor, index = 1, StringValues.serializer(), value.headers)
    }
  }

  override fun deserialize(decoder: Decoder): PartData.BinaryChannelItem {
    return decoder.decodeStructure(descriptor) {
      lateinit var binaryChannel: ByteReadChannel
      lateinit var headers: Headers
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 ->
            binaryChannel =
              decodeSerializableElement(descriptor, index, ByteReadChannel.serializer())
          1 ->
            headers =
              decodeSerializableElement(descriptor, index, StringValues.serializer()).toHeaders()
          else -> break
        }
      }
      PartData.BinaryChannelItem({ binaryChannel }, headers)
    }
  }
}
