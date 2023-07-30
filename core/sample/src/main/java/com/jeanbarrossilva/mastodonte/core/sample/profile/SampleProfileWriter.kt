package com.jeanbarrossilva.mastodonte.core.sample.profile

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.profile.type.followable.Follow
import com.jeanbarrossilva.mastodonte.core.sample.profile.type.editable.replacingOnceBy
import com.jeanbarrossilva.mastodonte.core.sample.profile.type.followable.SampleFollowableProfile
import kotlinx.coroutines.flow.update

/** Performs [Profile]-related writing operations. **/
object SampleProfileWriter {
    /**
     * Inserts the given [profile], replacing the existing one that has the same [ID][Profile.id] if
     * it's there.
     *
     * @param profile [Profile] to be added.
     **/
    fun insert(profile: Profile) {
        delete(profile.id)
        SampleProfileProvider.profilesFlow.update { it + profile }
    }

    /** Resets this [SampleProfileProvider] to its default state. **/
    fun reset() {
        SampleProfileProvider.profilesFlow.value = SampleProfileProvider.defaultProfiles
    }

    /**
     * Updates the [follow][SampleFollowableProfile.follow] status of the [Profile] whose
     * [ID][SampleFollowableProfile.id] is equal the given [id].
     *
     * @param id [Profile]'s [ID][SampleFollowableProfile.id].
     * @param follow [Follow] status to update the [SampleFollowableProfile.follow] to.
     **/
    internal suspend fun <T : Follow> updateFollow(id: String, follow: T) {
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
        SampleProfileProvider.profilesFlow.update { profiles ->
            profiles
                .toMutableList()
                .apply {
                    removeIf { profile ->
                        profile.id == id
                    }
                }
                .toList()
        }
    }

    /**
     * Replaces the currently existing [Profile] identified as [id] by its updated version.
     *
     * @param id ID of the [Profile] to be updated.
     * @param update Changes to be made to the existing [Profile].
     * @throws ProfileProvider.NonexistentProfileException If no [Profile] with such
     * [ID][Profile.id] exists.
     **/
    private suspend fun update(id: String, update: Profile.() -> Profile) {
        if (SampleProfileProvider.contains(id)) {
            SampleProfileProvider.profilesFlow.update { profiles ->
                profiles.replacingOnceBy(update) { profile ->
                    profile.id == id
                }
            }
        } else {
            throw ProfileProvider.NonexistentProfileException(id)
        }
    }
}
