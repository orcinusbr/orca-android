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

package com.jeanbarrossilva.orca.core.mastodon.client.test

import assertk.Assert
import assertk.assertThat
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders

/**
 * Creates an [Assert] on the [response]'s authorization [HttpHeader].
 *
 * @param response [HttpResponse] whose header will be the basis of the [Assert].
 */
internal fun assertThatRequestAuthorizationHeaderOf(response: HttpResponse): Assert<String?> {
  return assertThat(response.request.headers[HttpHeaders.Authorization])
}
