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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.platform.cache.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * [ProfileProvider] that either requests [MastodonProfile]s to the API or retrieves cached ones if
 * they're available.
 *
 * @param cache [Cache] of [MastodonProfile] by which [MastodonProfile]s will be obtained.
 */
class MastodonProfileProvider internal constructor(private val cache: Cache<Profile>) :
  ProfileProvider() {
  override suspend fun contains(id: String): Boolean {
    return true
  }

  override suspend fun onProvide(id: String): Flow<Profile> {
    val profile = cache.get(id)
    return flowOf(profile)
  }
}
