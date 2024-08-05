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

package br.com.orcinus.core.test.auth

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.test.DefaultAuthenticator
import br.com.orcinus.orca.core.test.auth.AuthorizerBuilder
import java.util.UUID
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class AuthorizerBuilderTests {
  @Test
  fun buildsAnAuthorizerWhoseBeforeCallbackIsCalledPriorToTheProvisioningOfTheAuthorizationCode() {
    runTest {
      var hasBeforeCallbackBeenCalled = false
      val authorizer = AuthorizerBuilder().before { hasBeforeCallbackBeenCalled = true }.build()
      DefaultAuthenticator(authorizer).authenticate()
      assertThat(hasBeforeCallbackBeenCalled).isTrue()
    }
  }

  @Test
  fun buildsAnAuthorizerWithTheDefaultAuthorizationCodeByDefault() {
    runTest {
      val authorizer = AuthorizerBuilder().build()
      DefaultAuthenticator(authorizer) {
          assertThat(it).isEqualTo(AuthorizerBuilder.DEFAULT_AUTHORIZATION_CODE)
        }
        .authenticate()
    }
  }

  @Test
  fun buildsAnAuthorizerWithTheSpecifiedAuthorizationCode() {
    runTest {
      val authorizationCode = UUID.randomUUID().toString()
      val authorizer = AuthorizerBuilder().authorizationCode(authorizationCode).build()
      DefaultAuthenticator(authorizer) { assertThat(it).isEqualTo(authorizationCode) }
        .authenticate()
    }
  }
}
