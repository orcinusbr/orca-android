package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.core.feed.profile.type.followable.Follow

/** Whether this [Follow] status denotes a following. */
internal val Follow.isFollowingType
  get() = this == Follow.Private.following() || this == Follow.Public.following()
