package com.jeanbarrossilva.orca.core.sample.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import java.net.URL

/** [ProfileSearchResult] returned by [sample]'s getter. */
private val sampleProfileSearchResult =
  ProfileSearchResult(
    Author.sample.id,
    Author.sample.account,
    avatarURL =
      URL("https://en.gravatar.com/userimage/153558542/08942ba9443ce68bf66345a2e6db656e.png"),
    Author.sample.name,
    Author.sample.profileURL
  )

/** A sample [ProfileSearchResult]. */
val ProfileSearchResult.Companion.sample
  get() = sampleProfileSearchResult

/** [ProfileSearchResult] samples. */
val ProfileSearchResult.Companion.samples
  get() = List(64) { sample }
