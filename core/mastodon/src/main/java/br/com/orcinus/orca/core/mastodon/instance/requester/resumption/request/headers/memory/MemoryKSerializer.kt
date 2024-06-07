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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.memory

import br.com.orcinus.orca.core.mastodon.instance.requester.InternalRequesterApi
import io.ktor.utils.io.bits.Memory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * [KSerializer] for serializing and deserializing and [Memory], returned by
 * [Memory.Companion.serializer].
 */
private object MemoryKSerializer : KSerializer<Memory> {
  override val descriptor =
    buildClassSerialDescriptor(MemoryKSerializer::class.java.name) {
      element("buffer", ByteBufferKSerializer.descriptor)
    }

  override fun serialize(encoder: Encoder, value: Memory) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(descriptor, index = 0, ByteBufferKSerializer, value.buffer)
    }
  }

  override fun deserialize(decoder: Decoder): Memory {
    return decoder.decodeStructure(descriptor) {
      Memory(
        decodeSerializableElement(
          descriptor,
          index = decodeElementIndex(descriptor),
          ByteBufferKSerializer
        )
      )
    }
  }
}

/** [KSerializer] for serializing and deserializing a [Memory]. */
@InternalRequesterApi
internal fun Memory.Companion.serializer(): KSerializer<Memory> {
  return MemoryKSerializer
}
