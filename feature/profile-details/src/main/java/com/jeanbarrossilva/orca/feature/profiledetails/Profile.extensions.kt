package com.jeanbarrossilva.orca.feature.profiledetails

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverterFactory
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
