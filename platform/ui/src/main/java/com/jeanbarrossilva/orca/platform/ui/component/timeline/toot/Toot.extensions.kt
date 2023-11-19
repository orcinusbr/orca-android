package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this [Toot] into a [Flow] of [SampleTootPreview].
 *
 * @param colors [Colors] by which the emitted [SampleTootPreview]s' [SampleTootPreview.text] can be
 *   colored.
 */
fun Toot.toTootPreviewFlow(colors: Colors): Flow<TootPreview> {
  return combine(
    comment.countFlow,
    favorite.isEnabledFlow,
    favorite.countFlow,
    reblog.isEnabledFlow,
    reblog.countFlow
  ) { _, _, _, _, _ ->
    toTootPreview(colors)
  }
}

/** Converts this [Toot] into a [SampleTootPreview]. */
@Composable
internal fun Toot.toTootPreview(): TootPreview {
  return toTootPreview(OrcaTheme.colors)
}

/**
 * Converts this [Toot] into a [SampleTootPreview].
 *
 * @param colors [Colors] by which the resulting [SampleTootPreview]'s [SampleTootPreview.text] can
 *   be colored.
 */
internal fun Toot.toTootPreview(colors: Colors): TootPreview {
  return TootPreview(
    id,
    author.avatarLoader,
    author.name,
    author.account,
    if (this is Reblog) reblogger.name else null,
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
