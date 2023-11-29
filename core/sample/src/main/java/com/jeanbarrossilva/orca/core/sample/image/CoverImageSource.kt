package com.jeanbarrossilva.orca.core.sample.image

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight

/**
 * [SampleImageSource] of [Post]s' [content][Post.content]s' [highlight][Content.highlight]s'
 * [headline][Highlight.headline]s' covers.
 */
sealed class CoverImageSource : SampleImageSource() {
  /** [CoverImageSource] of the default sample [Post]. */
  data object Default : CoverImageSource()
}
