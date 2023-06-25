package com.jeanbarrossilva.mastodonte.core.inmemory.profile

import com.jeanbarrossilva.mastodonte.core.inmemory.profile.follow.InMemoryFollowableProfile
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Account
import java.net.URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** Central for all [InMemoryProfile]-related reading and writing operations. **/
object InMemoryProfileDao : ProfileRepository {
    private var profilesFlow = MutableStateFlow(listOf(Profile.sample))

    override suspend fun get(id: String): Flow<Profile?> {
        return profilesFlow.map { profiles ->
            profiles.find { profile ->
                profile.id == id
            }
        }
    }

    /**
     * Inserts a [Profile].
     *
     * @param id Unique identifier.
     * @param account Unique identifier within an instance.
     * @param avatarURL [URL] that leads to the avatar image.
     * @param name Name to be displayed.
     * @param bio Describes who the owner is and/or provides information regarding the [Profile].
     * @param follow Current [Follow] status.
     * @param followerCount Amount of followers.
     * @param followingCount Amount of following.
     * @param url [URL] that leads to the webpage of the instance through which the [Profile] can
     * be accessed.
     **/
    fun <T : Follow> insert(
        id: String,
        account: Account,
        avatarURL: URL,
        name: String,
        bio: String,
        follow: T,
        followerCount: Int,
        followingCount: Int,
        url: URL
    ) {
        val profile = object : InMemoryFollowableProfile<T>() {
            override val id = id
            override val account = account
            override val avatarURL = avatarURL
            override val name = name
            override val bio = bio
            override val followerCount = followerCount
            override val followingCount = followingCount
            override val url = url
            override var follow: T = follow
        }
        insert(profile)
    }

    /**
     * Updates the [FollowableProfile.follow] of the [Profile] whose [ID][FollowableProfile.id] is
     * equal the given [profileID].
     *
     * @param profileID [Profile]'s [ID][FollowableProfile.id].
     * @param follow [Follow] to update the [FollowableProfile.follow] to.
     **/
    fun updateFollow(profileID: String, follow: Follow) {
        profilesFlow.value = profilesFlow.value.replacingOnceBy({
            apply {
                @Suppress("UNCHECKED_CAST")
                (this as InMemoryFollowableProfile<Follow>).follow = follow
            }
        }) {
            it.id == profileID
        }
    }

    /**
     * Inserts the given [profile].
     *
     * @param profile [Profile] to be added.
     **/
    private fun insert(profile: Profile) {
        profilesFlow.value = profilesFlow.value + profile
    }
}
