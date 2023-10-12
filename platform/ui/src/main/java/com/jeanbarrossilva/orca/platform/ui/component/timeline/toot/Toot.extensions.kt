package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this [Toot] into a [Flow] of [TootPreview].
 *
 * @param colors [Colors] by which the emitted [TootPreview]s' [TootPreview.text] can be colored.
 */
fun Toot.toTootPreviewFlow(colors: Colors): Flow<TootPreview> {
  val body = content.text.toAnnotatedString(colors)
  return combine(
    comment.countFlow,
    favorite.isEnabledFlow,
    favorite.countFlow,
    reblog.isEnabledFlow,
    reblog.countFlow
  ) { commentCount, isFavorite, favoriteCount, isReblogged, reblogCount ->
    TootPreview(
      id,
      author.avatarURL,
      author.name,
      author.account,
      body,
      content.highlight,
      publicationDateTime,
      commentCount,
      isFavorite,
      favoriteCount,
      isReblogged,
      reblogCount,
      url
    )
  }
}

/** Converts this [Toot] into a [TootPreview]. */
@Composable
internal fun Toot.toTootPreview(): TootPreview {
  return toTootPreview(OrcaTheme.colors)
}

/**
 * Converts this [Toot] into a [TootPreview].
 *
 * @param colors [Colors] by which the resulting [TootPreview]'s [TootPreview.text] can be colored.
 */
internal fun Toot.toTootPreview(colors: Colors): TootPreview {
  return TootPreview(
    id,
    author.avatarURL,
    author.name,
    author.account,
    content.text.toAnnotatedString(colors),
    content.highlight,
    publicationDateTime,
    comment.count,
    favorite.isEnabled,
    favorite.count,
    reblog.isEnabled,
    reblog.count,
    url
  )
}
