/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.core.mastodon.client.test.instance

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.test.TestAuthenticator
import com.jeanbarrossilva.orca.core.test.TestAuthorizer

/**
 * [InstanceProvider] that provides a [TestMastodonInstance].
 *
 * @param authorizer [TestAuthorizer] with which the user will be authorized.
 * @param authenticator [TestAuthenticator] through which authentication can be done.
 * @param authenticationLock [AuthenticationLock] by which features can be locked or unlocked by an
 *   authentication "wall".
 */
internal class TestMastodonInstanceProvider(
  private val authorizer: TestAuthorizer,
  private val authenticator: TestAuthenticator,
  private val authenticationLock: AuthenticationLock<TestAuthenticator>
) : InstanceProvider {
  override fun provide(): TestMastodonInstance {
    return TestMastodonInstance(authorizer, authenticator, authenticationLock)
  }
}
