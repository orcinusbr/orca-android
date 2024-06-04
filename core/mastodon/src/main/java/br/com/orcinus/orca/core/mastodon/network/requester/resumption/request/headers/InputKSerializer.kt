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

package br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers

import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.memory.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.pool.ObjectPoolKSerializer
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.pool.map
import io.ktor.util.copy
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.internal.ChunkBuffer
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.pool.ObjectPool
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * [KSerializer] for serializing and deserializing an [Input], returned by
 * [Input.Companion.serializer].
 */
private object InputKSerializer : KSerializer<Input> {
  override val descriptor =
    buildClassSerialDescriptor(InputKSerializer::class.java.name) {
      element("head", ChunkBuffer.serializer().descriptor)
      element("pool", ObjectPoolKSerializer.forChunkBuffer.descriptor)
    }

  override fun serialize(encoder: Encoder, value: Input) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(
        descriptor,
        index = 0,
        ChunkBuffer.serializer(),
        value.pool.borrow()
      )
      encodeSerializableElement(
        descriptor,
        index = 1,
        ObjectPoolKSerializer.forChunkBuffer,
        value.pool
      )
    }
  }

  override fun deserialize(decoder: Decoder): Input {
    return decoder.decodeStructure(descriptor) {
      lateinit var head: ChunkBuffer
      lateinit var pool: ObjectPool<ChunkBuffer>
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> head = decodeSerializableElement(descriptor, index, ChunkBuffer.serializer())
          1 ->
            pool =
              decodeSerializableElement(descriptor, index, ObjectPoolKSerializer.forChunkBuffer)
          else -> break
        }
      }
      buildPacket {
        writeFully(head.memory.buffer.copy(pool.map { it.memory.buffer }, head.referenceCount))
      }
    }
  }
}

/** [KSerializer] for serializing and deserializing an [Input]. */
@InternalNetworkApi
internal fun Input.Companion.serializer(): KSerializer<Input> {
  return InputKSerializer
}
