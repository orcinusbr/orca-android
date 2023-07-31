package com.jeanbarrossilva.mastodonte.core.sample.feed.profile

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.feed.profile.ProfileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** [ProfileProvider] that provides sample [Profile]s. **/
object SampleProfileProvider : ProfileProvider() {
    /** [Profile]s present that are present by default. **/
    internal val defaultProfiles = listOf(Profile.sample)

    /** [MutableStateFlow] that provides the [Profile]s. **/
    internal val profilesFlow = MutableStateFlow(defaultProfiles)

    public override suspend fun contains(id: String): Boolean {
        val ids = profilesFlow.value.map(Profile::id)
        return id in ids
    }

    override suspend fun onProvide(id: String): Flow<Profile> {
        return profilesFlow.map { profiles ->
            profiles.first { profile ->
                profile.id == id
            }
        }
    }
}
