package com.jeanbarrossilva.mastodon.feature.profiledetails.conversion

import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodonte.core.profile.Profile

/** Converts a [Profile] into a [ProfileDetails] through [convert]. **/
internal abstract class ProfileConverter {
    /** [ProfileConverter] to fallback to in case this one's [convert] returns `null`. **/
    abstract val next: ProfileConverter?

    /**
     * Converts the given [profile] into [ProfileDetails].
     *
     * @param profile [Profile] to convert into [ProfileDetails].
     **/
    fun convert(profile: Profile): ProfileDetails? {
        return onConvert(profile) ?: next?.convert(profile)
    }

    /**
     * Converts the given [profile] into [ProfileDetails].
     *
     * Returning `null` signals that this [ProfileConverter] cannot perform the conversion and that
     * the operation should be delegated to the [next] one.
     *
     * @param profile [Profile] to convert into [ProfileDetails].
     **/
    protected abstract fun onConvert(profile: Profile): ProfileDetails?
}
