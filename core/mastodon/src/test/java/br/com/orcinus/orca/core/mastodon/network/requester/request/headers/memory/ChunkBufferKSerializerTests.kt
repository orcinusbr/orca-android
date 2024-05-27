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

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.utils.io.bits.Memory
import io.ktor.utils.io.core.ChunkBuffer
import io.ktor.utils.io.core.internal.ChunkBuffer
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject

internal class ChunkBufferKSerializerTests {
  @Test
  fun serializes() {
    assertThat(
        Json.encodeToString(ChunkBuffer.serializer(), ChunkBuffer(ByteBuffer.wrap(byteArrayOf(0))))
      )
      .isEqualTo(
        buildJsonObject {
            put(
              @OptIn(ExperimentalSerializationApi::class)
              ChunkBuffer.serializer().descriptor.getElementName(0),
              Json.encodeToJsonElement(Memory.serializer(), Memory(ByteBuffer.wrap(byteArrayOf(0))))
            )
          }
          .toString()
      )
  }

  @Test
  fun deserializes() {
    assertThat(
        Json.decodeFromString(
            ChunkBuffer.serializer(),
            buildJsonObject {
                put(
                  @OptIn(ExperimentalSerializationApi::class)
                  ChunkBuffer.serializer().descriptor.getElementName(0),
                  Json.encodeToJsonElement(
                    Memory.serializer(),
                    Memory(ByteBuffer.wrap(byteArrayOf(0)))
                  )
                )
              }
              .toString()
          )
          .memory
      )
      .isEqualTo(Memory(ByteBuffer.wrap(byteArrayOf(0))))
  }
}
