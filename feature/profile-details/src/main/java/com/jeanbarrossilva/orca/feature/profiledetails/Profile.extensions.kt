package com.jeanbarrossilva.orca.feature.profiledetails

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.ProfileConverterFactory
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import kotlinx.coroutines.CoroutineScope

/**
 * Converts this core [ProfileDetails] into [ProfileDetails].
 *
 * @param coroutineScope [CoroutineScope] through which converted [Profile]-related suspending will
 *   be performed.
 * @param colors [Colors] by which visuals can be colored.
 */
internal fun Profile.toProfileDetails(
  coroutineScope: CoroutineScope,
  colors: Colors
): ProfileDetails {
  val details = ProfileConverterFactory.create(coroutineScope).convert(this, colors)
  return requireNotNull(details)
}
