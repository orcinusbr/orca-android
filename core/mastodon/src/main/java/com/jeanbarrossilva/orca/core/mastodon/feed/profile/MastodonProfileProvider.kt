/*
 * Copyright © 2023–2024 Orcinus
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
