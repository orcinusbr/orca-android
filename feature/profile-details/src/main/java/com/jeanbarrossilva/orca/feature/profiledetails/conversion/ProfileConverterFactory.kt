package com.jeanbarrossilva.orca.feature.profiledetails.conversion

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.DefaultProfileConverter
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.EditableProfileConverter
import com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable.FollowableProfileConverter
import kotlinx.coroutines.CoroutineScope

/** Creates an instance of a [ProfileConverter] through [create]. */
internal object ProfileConverterFactory {
  /**
   * Creates a [ProfileConverter].
   *
   * @param coroutineScope [CoroutineScope] through which converted [Profile]-related suspending
   *   will be performed.
   */
  fun create(coroutineScope: CoroutineScope): ProfileConverter {
    val defaultConverter = DefaultProfileConverter(next = null)
    val editableConverter = EditableProfileConverter(next = defaultConverter)
    return FollowableProfileConverter(coroutineScope, next = editableConverter)
  }
}
