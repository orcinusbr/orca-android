package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/** [Profile] returned by [sample]'s getter. */
@Suppress("SpellCheckingInspection")
private val sampleProfile: Profile =
  object : SampleProfile {
    override val id = Author.sample.id
    override val account = Author.sample.account
    override val avatarURL =
      URL("https://en.gravatar.com/userimage/153558542/08942ba9443ce68bf66345a2e6db656e.png")
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
