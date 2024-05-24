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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.memory

import br.com.orcinus.orca.core.mastodon.network.requester.InternalRequesterApi
import io.ktor.utils.io.bits.Memory
import io.ktor.utils.io.core.ChunkBuffer
import io.ktor.utils.io.core.internal.ChunkBuffer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * [KSerializer] for serializing and deserializing a [ChunkBuffer], returned by
 * [ChunkBuffer.Companion.serializer].
 */
private object ChunkBufferKSerializer : KSerializer<ChunkBuffer> {
  override val descriptor: SerialDescriptor =
    buildClassSerialDescriptor(ChunkBufferKSerializer::class.java.name) {
      element("memory", ByteBufferKSerializer.descriptor)
    }

  override fun serialize(encoder: Encoder, value: ChunkBuffer) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(descriptor, index = 0, Memory.serializer(), value.memory)
    }
  }

  override fun deserialize(decoder: Decoder): ChunkBuffer {
    return decoder.decodeStructure(descriptor) {
      ChunkBuffer(
        decodeSerializableElement(descriptor, decodeElementIndex(descriptor), Memory.serializer())
          .buffer
      )
    }
  }
}

/** [KSerializer] for serializing and deserializing a [ChunkBuffer]. */
@InternalRequesterApi
internal fun ChunkBuffer.Companion.serializer(): KSerializer<ChunkBuffer> {
  return ChunkBufferKSerializer
}
