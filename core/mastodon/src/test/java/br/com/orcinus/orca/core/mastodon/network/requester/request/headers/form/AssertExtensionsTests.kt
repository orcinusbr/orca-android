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

package br.com.orcinus.orca.core.mastodon.network.requester.request.headers.form

import assertk.assertThat
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class AssertExtensionsTests {
  @Test
  fun passesWhenBinaryChannelItemsAreEquivalent() {
    assertThat(PartData.BinaryChannelItem({ ByteReadChannel.Empty }, Headers.Empty))
      .isEquivalentTo(PartData.BinaryChannelItem({ ByteReadChannel.Empty }, Headers.Empty))
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenBinaryChannelItemsAreInequivalent() {
    assertThat(
        PartData.BinaryChannelItem({ ByteReadChannel(byteArrayOf(0b00000001)) }, Headers.Empty)
      )
      .isEquivalentTo(PartData.BinaryChannelItem({ ByteReadChannel.Empty }, Headers.Empty))
  }
}
