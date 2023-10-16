package com.jeanbarrossilva.orca.core.sample.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.SampleCoreModule
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.orca.core.sample.imageLoaderProvider
import com.jeanbarrossilva.orca.std.injector.Injector

/** [ProfileSearchResult] returned by [sample]'s getter. */
private val sampleProfileSearchResult =
  ProfileSearchResult(
    Author.sample.id,
    Author.sample.account,
    avatarLoader =
      Injector.from<SampleCoreModule>().imageLoaderProvider().provide(AuthorImageSource.Default),
    Author.sample.name,
    Author.sample.profileURL
  )

/** A sample [ProfileSearchResult]. */
val ProfileSearchResult.Companion.sample
  get() = sampleProfileSearchResult

/** [ProfileSearchResult] samples. */
val ProfileSearchResult.Companion.samples
  get() = List(64) { sample }
