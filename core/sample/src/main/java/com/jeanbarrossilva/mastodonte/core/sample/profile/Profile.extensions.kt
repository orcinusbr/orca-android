package com.jeanbarrossilva.mastodonte.core.sample.profile

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.sample.profile.edit.SampleEditableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.sample

/** [Profile] returned by [sample]'s getter. **/
@Suppress("SpellCheckingInspection")
private val sampleProfile = SampleEditableProfile.createInstance(
    Author.sample.id,
    Author.sample.account,
    Author.sample.avatarURL,
    Author.sample.name,
    bio = "Co-founder @ Grupo Estoa, software engineer, author, writer and content creator; " +
        "neuroscience, quantum physics and philosophy enthusiast.",
    followerCount = 4_096,
    followingCount = 64,
    Author.sample.profileURL
)

/** A sample [Profile]. **/
val Profile.Companion.sample: Profile
    get() = sampleProfile
