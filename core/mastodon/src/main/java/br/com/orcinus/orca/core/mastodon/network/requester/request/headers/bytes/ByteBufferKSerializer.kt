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
import java.nio.ByteBuffer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/** [KSerializer] for serializing and deserializing a [ByteBuffer]. */
@InternalRequesterApi
internal object ByteBufferKSerializer : KSerializer<ByteBuffer> {
  override val descriptor =
    buildClassSerialDescriptor(ByteBufferKSerializer::class.java.name) {
      element<ByteArray>("array")
    }

  override fun serialize(encoder: Encoder, value: ByteBuffer) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(descriptor, index = 0, ByteArraySerializer(), value.array())
    }
  }

  override fun deserialize(decoder: Decoder): ByteBuffer {
    return decoder.decodeStructure(descriptor) {
      ByteBuffer.wrap(
        decodeSerializableElement(descriptor, decodeElementIndex(descriptor), ByteArraySerializer())
      )
    }
  }
}
