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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.form.hasSameContentAs
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.memory.serializer
import br.com.orcinus.orca.core.mastodon.network.requester.request.headers.pool.ObjectPoolKSerializer
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.internal.ChunkBuffer
import io.ktor.utils.io.streams.asInput
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject

internal class InputKSerializerTests {
  private val input = byteArrayOf(0).inputStream().asInput()

  @AfterTest
  fun tearDown() {
    input.close()
  }

  @Test
  fun serializes() {
    assertThat(Json.encodeToString(Input.serializer(), input))
      .isEqualTo(
        @OptIn(ExperimentalSerializationApi::class)
        buildJsonObject {
            put(
              Input.serializer().descriptor.getElementName(0),
              Json.encodeToJsonElement(ChunkBuffer.serializer(), input.pool.borrow())
            )
            put(
              Input.serializer().descriptor.getElementName(1),
              Json.encodeToJsonElement(ObjectPoolKSerializer.forChunkBuffer, ChunkBuffer.Pool)
            )
          }
          .toString()
      )
  }

  @Test
  fun deserializes() {
    assertThat(
        Json.decodeFromString(
          Input.serializer(),
          @OptIn(ExperimentalSerializationApi::class)
          buildJsonObject {
              put(
                Input.serializer().descriptor.getElementName(0),
                Json.encodeToJsonElement(ChunkBuffer.serializer(), input.pool.borrow())
              )
              put(
                Input.serializer().descriptor.getElementName(1),
                Json.encodeToJsonElement(ObjectPoolKSerializer.forChunkBuffer, ChunkBuffer.Pool)
              )
            }
            .toString()
        )
      )
      .hasSameContentAs(input)
  }
}
