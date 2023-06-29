package com.jeanbarrossilva.mastodonte.core.sample.profile

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Account
import com.jeanbarrossilva.mastodonte.core.sample.profile.edit.replacingOnceBy
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.SampleFollowableProfile
import java.net.URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/** Central for all [Profile]-related reading and writing operations. **/
object SampleProfileDao : ProfileRepository {
    /** [MutableStateFlow] that provides the [Profile]s. **/
    val profilesFlow = MutableStateFlow(listOf(Profile.sample))

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
        val profile = SampleFollowableProfile.createInstance(
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
        profilesFlow.value = profilesFlow
            .value
            .filterIsInstance<SampleFollowableProfile<Follow>>()
            .replacingOnceBy({
                apply {
                    this.follow = follow
                }
            }) {
                it.id == profileID
            }
    }

    /**
     * Whether a [Profile] whose [ID][Profile.id] equals to the given one is present.
     *
     * @param id ID of the [Profile] whose presence will be checked.
     **/
    internal suspend fun contains(id: String): Boolean {
        return get(id).first() != null
    }

    /** Removes all [Profile]s. **/
    internal fun clear() {
        profilesFlow.value = emptyList()
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
