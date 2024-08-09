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

package br.com.orcinus.orca.core.sample.feed.profile.search

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearcher
import br.com.orcinus.orca.core.feed.profile.search.toProfileSearchResult
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [ProfileSearcher] that searches through sample [Profile]s.
 *
 * @param provider [SampleProfileProvider] by which [Profile]s will be provided.
 */
class SampleProfileSearcher(private val provider: SampleProfileProvider) : ProfileSearcher() {
  override suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>> {
    return provider.profilesFlow.map { profiles ->
      profiles.map(Profile::toProfileSearchResult).filter { profile ->
        profile.account.toString().contains(query, ignoreCase = true) ||
          profile.name.contains(query, ignoreCase = true)
      }
    }
  }
}
