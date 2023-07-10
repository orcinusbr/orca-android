package com.jeanbarrossilva.mastodonte.core.sample.profile

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.sample

/** [Profile] returned by [sample]'s getter. **/
@Suppress("SpellCheckingInspection")
private val sampleProfile: Profile = object : SampleProfile {
    override val id = Author.sample.id
    override val account = Author.sample.account
    override val avatarURL = Author.sample.avatarURL
    override val name = Author.sample.name
    override val bio = "Co-founder @ Grupo Estoa, software engineer, author, writer and content " +
        "creator; neuroscience, quantum physics and philosophy enthusiast."
    override val followerCount = 1_024
    override val followingCount = 64
    override val url = Author.sample.profileURL
}

/** A sample [Profile]. **/
val Profile.Companion.sample
    get() = sampleProfile
