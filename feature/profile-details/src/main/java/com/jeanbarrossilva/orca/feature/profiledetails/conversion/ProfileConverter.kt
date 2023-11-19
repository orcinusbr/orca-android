package com.jeanbarrossilva.orca.feature.profiledetails.conversion

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails

/** Converts a [Profile] into a [ProfileDetails] through [convert]. */
internal abstract class ProfileConverter {
  /** [ProfileConverter] to fallback to in case this one's [convert] returns `null`. */
  abstract val next: ProfileConverter?

  /**
   * Converts the given [profile] into [ProfileDetails].
   *
   * @param profile [Profile] to convert into [ProfileDetails].
   * @param colors [Colors] by which visuals can be colored.
   */
  fun convert(profile: Profile, colors: Colors): ProfileDetails? {
    return onConvert(profile, colors) ?: next?.convert(profile, colors)
  }

  /**
   * Converts the given [profile] into [ProfileDetails].
   *
   * Returning `null` signals that this [ProfileConverter] cannot perform the conversion and that
   * the operation should be delegated to the [next] one.
   *
   * @param profile [Profile] to convert into [ProfileDetails].
   * @param colors [Colors] by which visuals can be colored.
   */
  protected abstract fun onConvert(profile: Profile, colors: Colors): ProfileDetails?
}
