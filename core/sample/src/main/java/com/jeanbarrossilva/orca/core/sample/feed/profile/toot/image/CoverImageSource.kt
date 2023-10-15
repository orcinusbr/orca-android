package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight

/**
 * [SampleImageSource] of [Toot]s' [content][Toot.content]s' [highlight][Content.highlight]s'
 * [headline][Highlight.headline]s' covers.
 */
sealed class CoverImageSource : SampleImageSource() {
  /** [CoverImageSource] of the default sample [Toot]. */
  data object Default : CoverImageSource()
}
