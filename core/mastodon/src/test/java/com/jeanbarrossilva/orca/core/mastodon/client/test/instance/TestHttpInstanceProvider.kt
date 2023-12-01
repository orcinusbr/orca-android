/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
internal class TestHttpInstanceProvider(
  private val authorizer: TestAuthorizer,
  private val authenticator: TestAuthenticator,
  private val authenticationLock: AuthenticationLock<TestAuthenticator>
) : InstanceProvider {
  override fun provide(): TestMastodonInstance {
    return TestMastodonInstance(authorizer, authenticator, authenticationLock)
  }
}
