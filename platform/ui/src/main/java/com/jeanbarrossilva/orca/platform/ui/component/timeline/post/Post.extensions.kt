package com.jeanbarrossilva.orca.platform.ui.component.timeline.post

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this [Post] into a [Flow] of [PostPreview].
 *
 * @param colors [Colors] by which the emitted [PostPreview]s' [PostPreview.text] can be colored.
 */
fun Post.toPostPreviewFlow(colors: Colors): Flow<PostPreview> {
  return combine(
    comment.countFlow,
    favorite.isEnabledFlow,
    favorite.countFlow,
    repost.isEnabledFlow,
    repost.countFlow
  ) { _, _, _, _, _ ->
    toPostPreview(colors)
  }
}

/** Converts this [Post] into a [PostPreview]. */
@Composable
internal fun Post.toPostPreview(): PostPreview {
  return toPostPreview(AutosTheme.colors)
}

/**
 * Converts this [Post] into a [PostPreview].
 *
 * @param colors [Colors] by which the resulting [PostPreview]'s [PostPreview.text] can be colored.
 */
internal fun Post.toPostPreview(colors: Colors): PostPreview {
  return PostPreview(
    id,
    author.avatarLoader,
    author.name,
    author.account,
    if (this is Repost) reposter.name else null,
    content.text.toAnnotatedString(colors),
    content.highlight,
    publicationDateTime,
    comment.count,
    favorite.isEnabled,
    favorite.count,
    repost.isEnabled,
    repost.count,
    url
  )
}
