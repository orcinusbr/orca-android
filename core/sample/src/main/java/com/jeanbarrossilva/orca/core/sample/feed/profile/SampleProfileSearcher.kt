package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileSearcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** [ProfileSearcher] that searches through the sample [Profile]s. **/
object SampleProfileSearcher : ProfileSearcher() {
    override suspend fun onSearch(query: String): Flow<List<Profile>> {
        return SampleProfileProvider.profilesFlow.map { profiles ->
            profiles.filter { profile ->
                profile.account.toString().contains(query, ignoreCase = true) ||
                    profile.name.contains(query, ignoreCase = true)
            }
        }
    }
}
