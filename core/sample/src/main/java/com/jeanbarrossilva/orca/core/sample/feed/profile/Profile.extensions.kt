package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/** [Profile] returned by [sample]'s getter. */
@Suppress("SpellCheckingInspection")
private val sampleProfile: Profile =
  object : SampleProfile {
    override val id = Author.sample.id
    override val account = Author.sample.account
    override val avatarURL = Author.sample.avatarURL
    override val name = Author.sample.name
    override val bio =
      StyledString(
        "Co-founder @ Grupo Estoa, software engineer, author, writer and content creator; " +
          "neuroscience, quantum physics and philosophy enthusiast."
      )
    override val followerCount = 1_024
    override val followingCount = 64
    override val url = Author.sample.profileURL
  }

/** A sample [Profile]. */
val Profile.Companion.sample
  get() = sampleProfile
