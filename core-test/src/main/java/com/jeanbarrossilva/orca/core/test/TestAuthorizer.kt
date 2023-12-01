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

package com.jeanbarrossilva.orca.core.test

import com.jeanbarrossilva.orca.core.auth.Authorizer

/**
 * [Authorizer] that provides a fixed authorization code.
 *
 * @param onAuthorize Operation to be performed when [authorize] is called.
 * @see AUTHORIZATION_CODE
 */
class TestAuthorizer(private val onAuthorize: () -> Unit = {}) : Authorizer() {
  override suspend fun authorize(): String {
    onAuthorize()
    return AUTHORIZATION_CODE
  }

  companion object {
    /** Authorization code provided by [authorize]. */
    const val AUTHORIZATION_CODE = "authorization-code"
  }
}
