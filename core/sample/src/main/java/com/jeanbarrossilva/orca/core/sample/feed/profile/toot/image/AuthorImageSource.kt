package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author

/** [SampleImageSource] of [Author]s' avatars. */
sealed class AuthorImageSource : SampleImageSource() {
  data object Default : AuthorImageSource()

  data object Rambo : AuthorImageSource()
}
