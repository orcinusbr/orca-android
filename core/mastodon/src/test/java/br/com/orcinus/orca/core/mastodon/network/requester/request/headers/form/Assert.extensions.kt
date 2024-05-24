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

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.support.expected
import assertk.assertions.support.show
import io.ktor.http.Headers
import io.ktor.http.content.PartData
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.test.runTest

/**
 * Asserts that the binary channel item is equivalent to the given one.
 *
 * @param expected [PartData.BinaryChannelItem] to be compared to the actual one.
 */
internal fun Assert<PartData.BinaryChannelItem>.isEquivalentTo(
  expected: PartData.BinaryChannelItem
): Assert<PartData.BinaryChannelItem> {
  transform("provider") { it.provider() }
    .given {
      runTest {
        val actualRemainingBytes = it.readRemaining().readBytes()
        val expectedRemainingBytes = expected.provider().readRemaining().readBytes()
        if (!actualRemainingBytes.contentEquals(expectedRemainingBytes)) {
          expected(
            "provided, read bytes to be:${show(expectedRemainingBytes)} but was:" +
              show(actualRemainingBytes)
          )
        }
      }
    }
  transform("headers", PartData.BinaryChannelItem::headers).isEqualTo(Headers.Empty)
  return this
}
