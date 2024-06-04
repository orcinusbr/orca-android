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

package br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.pool

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.headers.memory.serializer
import io.ktor.utils.io.core.internal.ChunkBuffer
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.pool.useInstance
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

internal class ObjectPoolKSerializerTests {
  private val objectPool = objectPoolOf(production = ChunkBuffer::Empty)

  @AfterTest
  fun tearDown() {
    objectPool.close()
  }

  @Test
  fun serializes() {
    assertThat(Json.encodeToString(ObjectPoolKSerializer.forChunkBuffer, objectPool))
      .isEqualTo(
        @OptIn(ExperimentalSerializationApi::class)
        buildJsonObject {
            put(ObjectPoolKSerializer.forChunkBuffer.descriptor.getElementName(0), 1)
            put(
              ObjectPoolKSerializer.forChunkBuffer.descriptor.getElementName(1),
              Json.encodeToJsonElement(ChunkBuffer.serializer(), ChunkBuffer.Empty)
            )
          }
          .toString()
      )
  }

  @Test
  fun deserializes() {
    assertThat(
        Json.decodeFromString(
            ObjectPoolKSerializer.forChunkBuffer,
            @OptIn(ExperimentalSerializationApi::class)
            buildJsonObject {
                put(ObjectPoolKSerializer.forChunkBuffer.descriptor.getElementName(0), 1)
                put(
                  ObjectPoolKSerializer.forChunkBuffer.descriptor.getElementName(1),
                  Json.encodeToJsonElement(ChunkBuffer.serializer(), ChunkBuffer.Empty)
                )
              }
              .toString()
          )
          .useInstance { it }
          .readBytes()
      )
      .isEqualTo(objectPool.borrow().readBytes())
  }
}
