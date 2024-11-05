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

package br.com.orcinus.orca.core.mastodon.notification.service.security

import assertk.assertThat
import assertk.assertions.first
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.prop
import java.util.Base64
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class LocksmithTests {
  private val locksmith = Locksmith()
  private val base64Decoder: Base64.Decoder = Base64.getUrlDecoder()

  @Test
  fun publicKeyIs65ByteLong() {
    assertThat(locksmith)
      .prop(Locksmith::publicKey)
      .transform("decoded", base64Decoder::decode)
      .hasSize(65)
  }

  @Test
  fun publicKeyIsUncompressed() {
    assertThat(locksmith)
      .prop(Locksmith::publicKey)
      .transform("decoded", base64Decoder::decode)
      .prop(ByteArray::toSet)
      .first()
      .isEqualTo(Locksmith.UNCOMPRESSED_ELLIPTIC_CURVE_KEY_MARKER)
  }

  @Test
  fun publicKeyIsRandom() {
    assertThat(locksmith).prop(Locksmith::publicKey).isNotEqualTo(Locksmith().publicKey)
  }

  @Test
  fun authenticationKeyIs16ByteLong() {
    assertThat(locksmith)
      .prop(Locksmith::authenticationKey)
      .transform("decoded", base64Decoder::decode)
      .hasSize(16)
  }

  @Test
  fun authenticationKeyIsRandom() {
    assertThat(locksmith)
      .prop(Locksmith::authenticationKey)
      .isNotEqualTo(Locksmith().authenticationKey)
  }
}
