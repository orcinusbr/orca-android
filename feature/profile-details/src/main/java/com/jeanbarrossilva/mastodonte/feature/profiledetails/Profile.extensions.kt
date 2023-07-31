package com.jeanbarrossilva.mastodonte.feature.profiledetails

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.ProfileConverterFactory
import kotlinx.coroutines.CoroutineScope

/**
 * Converts this core [ProfileDetails] into [ProfileDetails].
 *
 * @param coroutineScope [CoroutineScope] through which converted [Profile]-related suspending will
 * be performed.
 **/
internal fun Profile.toProfileDetails(coroutineScope: CoroutineScope): ProfileDetails {
    val details = ProfileConverterFactory.create(coroutineScope).convert(this)
    return requireNotNull(details)
}
