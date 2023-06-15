package com.jeanbarrossilva.mastodonte.core.inmemory.profile

import com.jeanbarrossilva.mastodonte.core.profile.Account
import com.jeanbarrossilva.mastodonte.core.profile.Follow
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import java.net.URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** Central for all [InMemoryProfile]-related reading and writing operations. **/
object InMemoryProfileDao : ProfileRepository {
    private var profilesFlow = MutableStateFlow(emptyList<AnyInMemoryProfile>())

    override suspend fun get(id: String): Flow<AnyInMemoryProfile?> {
        return profilesFlow.map { profiles ->
            profiles.find { profile ->
                profile.id == id
            }
        }
    }

    /**
     * Adds an [InMemoryProfile.Public].
     *
     * @param id Unique identifier.
     * @param account Unique identifier within an instance.
     * @param avatarURL [URL] that leads to the avatar image.
     * @param name Name to be displayed.
     * @param bio Describes who the owner is and/or provides information regarding the
     * [InMemoryProfile].
     * @param follow Current [Follow] status.
     * @param followerCount Amount of followers.
     * @param followingCount Amount of following.
     * @param url [URL] that leads to the webpage of the instance through which the [Profile] can
     * be accessed.
     **/
    fun add(
        id: String,
        account: Account,
        avatarURL: URL,
        name: String,
        bio: String,
        follow: Follow,
        followerCount: Int,
        followingCount: Int,
        url: URL
    ) {
        val profile = InMemoryProfile(
            id,
            account,
            avatarURL,
            name,
            bio,
            follow,
            followerCount,
            followingCount,
            url
        )
        add(profile)
    }

    /**
     * Updates the [InMemoryProfile.follow] of the [InMemoryProfile] whose
     * [ID][InMemoryProfile.id] is equal to the given [profileID].
     *
     * @param profileID [InMemoryProfile]'s [ID][InMemoryProfile.id].
     * @param follow [Follow] to update the [InMemoryProfile.follow] to.
     **/
    fun updateFollow(profileID: String, follow: Follow) {
        profilesFlow.value = profilesFlow.value.replacingOnceBy({
            apply {
                @Suppress("UNCHECKED_CAST")
                (this as InMemoryProfile<Follow>).follow = follow
            }
        }) {
            it.id == profileID
        }
    }

    /**
     * Adds the given [profile].
     *
     * @param profile [InMemoryProfile] to be added.
     **/
    private fun add(profile: AnyInMemoryProfile) {
        profilesFlow.value = profilesFlow.value + profile
    }
}
