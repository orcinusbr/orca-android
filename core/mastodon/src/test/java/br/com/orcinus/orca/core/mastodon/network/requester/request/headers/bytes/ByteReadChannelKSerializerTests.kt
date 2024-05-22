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

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray

internal class ByteReadChannelKSerializerTests {
  @Test
  fun serializes() {
    assertThat(
        Json.encodeToString(
          ByteReadChannel.serializer(),
          ByteReadChannel(byteArrayOf(0b000001, 0b00000010))
        )
      )
      .isEqualTo(
        buildJsonObject {
            @OptIn(ExperimentalSerializationApi::class)
            putJsonArray(ByteReadChannel.Companion.serializer().descriptor.getElementName(0)) {
              add(0b00000001)
              add(0b00000010)
            }
          }
          .toString()
      )
  }

  @Test
  fun deserializes() {
    runTest {
      assertThat(
          Json.decodeFromString(
              ByteReadChannel.serializer(),
              buildJsonObject {
                  @OptIn(ExperimentalSerializationApi::class)
                  putJsonArray(
                    ByteReadChannel.Companion.serializer().descriptor.getElementName(0)
                  ) {
                    add(0b00000001)
                    add(0b00000010)
                  }
                }
                .toString()
            )
            .toByteArray()
        )
        .isEqualTo(byteArrayOf(0b000001, 0b00000010))
    }
  }
}
