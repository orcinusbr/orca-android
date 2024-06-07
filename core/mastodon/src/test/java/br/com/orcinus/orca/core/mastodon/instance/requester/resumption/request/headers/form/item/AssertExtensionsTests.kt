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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption.request.headers.form.item

import assertk.assertThat
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.streams.asInput
import java.io.ByteArrayInputStream
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class AssertExtensionsTests {
  @Test
  fun passesWhenBinaryChannelItemsHaveTheSameContent() {
    assertThat(ByteReadChannel.Empty).hasSameContentAs(ByteReadChannel.Empty)
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenBinaryChannelItemsHaveDifferentContents() {
    assertThat(ByteReadChannel(byteArrayOf(0))).hasSameContentAs(ByteReadChannel.Empty)
  }

  @Test
  fun passesWhenInputsHaveTheSameContent() {
    val buffer = byteArrayOf(0)
    assertThat(ByteArrayInputStream(buffer).asInput())
      .hasSameContentAs(ByteArrayInputStream(buffer).asInput())
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenInputsHaveDifferentContents() {
    assertThat(byteArrayOf(1).inputStream().asInput())
      .hasSameContentAs(byteArrayOf(0).inputStream().asInput())
  }
}
