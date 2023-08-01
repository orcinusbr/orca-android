package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample

/** [EditableProfile] returned by [sample]'s getter. **/
private val sampleEditableProfile: EditableProfile = SampleEditableProfile.createInstance(
    Profile.sample.id,
    Profile.sample.account,
    Profile.sample.avatarURL,
    Profile.sample.name,
    Profile.sample.bio,
    Profile.sample.followerCount,
    Profile.sample.followingCount,
    Profile.sample.url
)

/** A sample [EditableProfile]. **/
val EditableProfile.Companion.sample
    get() = sampleEditableProfile
