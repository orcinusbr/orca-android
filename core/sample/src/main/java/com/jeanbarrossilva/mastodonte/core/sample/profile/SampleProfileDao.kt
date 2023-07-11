package com.jeanbarrossilva.mastodonte.core.sample.profile

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.core.profile.follow.Follow
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.edit.replacingOnceBy
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.SampleFollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.sample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** Central for all [Profile]-related reading and writing operations. **/
object SampleProfileDao : ProfileRepository {
    /** [Profile]s present that are present by default. **/
    private val defaultProfiles = listOf<Profile>(FollowableProfile.sample)

    /** [MutableStateFlow] that provides the [Profile]s. **/
    val profilesFlow = MutableStateFlow(defaultProfiles)

    override suspend fun get(id: String): Flow<Profile?> {
        return profilesFlow.map { profiles ->
            profiles.find { profile ->
                profile.id == id
            }
        }
    }

    /**
     * Inserts the given [profile], replacing the existing one that has the same [ID][Profile.id] if
     * it's there.
     *
     * @param profile [Profile] to be added.
     **/
    fun insert(profile: Profile) {
        delete(profile.id)
        profilesFlow.value = profilesFlow.value + profile
    }

    /** Resets this [SampleProfileDao] to its default state. **/
    fun reset() {
        profilesFlow.value = defaultProfiles
    }

    /**
     * Whether a [Profile] whose [ID][Profile.id] equals to the given one is present.
     *
     * @param id ID of the [Profile] whose presence will be checked.
     **/
    internal operator fun contains(id: String): Boolean {
        val ids = profilesFlow.value.map(Profile::id)
        return id in ids
    }

    /**
     * Updates the [FollowableProfile.follow] of the [Profile] whose [ID][FollowableProfile.id] is
     * equal the given [id].
     *
     * @param id [Profile]'s [ID][FollowableProfile.id].
     * @param follow [Follow] to update the [FollowableProfile.follow] to.
     **/
    internal fun <T : Follow> updateFollow(id: String, follow: T) {
        update(id) {
            @Suppress("UNCHECKED_CAST")
            (this as SampleFollowableProfile<T>).copy(follow = follow)
        }
    }

    /**
     * Deletes the [Profile] whose [ID][Profile.id] equals to the given one.
     *
     * @param id ID of the [Profile] to be deleted.
     **/
    private fun delete(id: String) {
        profilesFlow.value = profilesFlow
            .value
            .toMutableList()
            .apply {
                removeIf {
                    it.id == id
                }
            }
            .toList()
    }

    /**
     * Replaces the currently existing [Profile] identified as [id] by its updated version.
     *
     * @param id ID of the [Profile] to be updated.
     * @param update Changes to be made to the existing [Profile].
     * @throws IllegalArgumentException If no [Profile] with such ID exists.
     **/
    private fun update(id: String, update: Profile.() -> Profile) {
        if (contains(id)) {
            profilesFlow.update { profiles ->
                profiles.replacingOnceBy(update) { profile ->
                    profile.id == id
                }
            }
        } else {
            throw IllegalArgumentException("Profile $id found.")
        }
    }
}
