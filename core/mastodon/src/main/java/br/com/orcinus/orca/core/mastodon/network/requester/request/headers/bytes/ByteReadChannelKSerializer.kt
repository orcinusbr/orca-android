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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.bytes

import br.com.orcinus.orca.core.mastodon.network.requester.InternalRequesterApi
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * [KSerializer] for serializing and deserializing a [ByteReadChannel], returned by
 * [ByteReadChannel.Companion.serializer].
 */
private object ByteReadChannelKSerializer : KSerializer<ByteReadChannel> {
  override val descriptor =
    buildClassSerialDescriptor(ByteReadChannelKSerializer::class.java.name) {
      element<ByteArray>("content")
    }

  override fun serialize(encoder: Encoder, value: ByteReadChannel) {
    val destination = runBlocking { value.toByteArray() }
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(descriptor, index = 0, ByteArraySerializer(), destination)
    }
  }

  override fun deserialize(decoder: Decoder): ByteReadChannel {
    return decoder.decodeStructure(descriptor) {
      ByteReadChannel(
        decodeSerializableElement(
          serialDescriptor<ByteArray>(),
          index = decodeElementIndex(descriptor),
          ByteArraySerializer()
        )
      )
    }
  }
}

/** [KSerializer] for serializing and deserializing a [ByteReadChannel]. */
@InternalRequesterApi
internal fun ByteReadChannel.Companion.serializer(): KSerializer<ByteReadChannel> {
  return ByteReadChannelKSerializer
}
