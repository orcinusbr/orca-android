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

package br.com.orcinus.orca.core.mastodon.notification.push.web

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isNotEqualTo
import assertk.assertions.prop
import kotlin.test.Test

internal class ClientKeysTests {
  @Test
  fun authenticationKeyIs16BytesLong() =
    assertThat(ClientKeys()).prop(ClientKeys::getAuthentication).hasSize(16)

  @Test
  fun generatesAuthenticationKeyWhenRefreshing() {
    val clientKeys = ClientKeys()
    val previousAuthenticationKey = clientKeys.authentication
    clientKeys.generate()
    assertThat(clientKeys)
      .prop(ClientKeys::getAuthentication)
      .isNotEqualTo(previousAuthenticationKey)
  }

  @Test
  fun generatesPrivateKeyWhenRefreshing() {
    val clientKeys = ClientKeys()
    val previousPrivateKey = clientKeys.private
    clientKeys.generate()
    assertThat(clientKeys).prop(ClientKeys::getPrivate).isNotEqualTo(previousPrivateKey)
  }

  @Test
  fun generatesPublicKeyWhenRefreshing() {
    val clientKeys = ClientKeys()
    val previousPublicKey = clientKeys.public
    clientKeys.generate()
    assertThat(clientKeys).prop(ClientKeys::getPublic).isNotEqualTo(previousPublicKey)
  }
}
