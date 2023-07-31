package com.jeanbarrossilva.mastodonte.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.sample

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
