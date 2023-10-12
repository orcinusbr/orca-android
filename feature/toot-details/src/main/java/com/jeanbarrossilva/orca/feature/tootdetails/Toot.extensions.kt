package com.jeanbarrossilva.orca.feature.tootdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Converts this core [Toot] into a [Flow] of [TootDetails].
 *
 * @param colors [Colors] by which the emitted [TootDetails]' [TootDetails.text] can be colored.
 */
internal fun Toot.toTootDetailsFlow(colors: Colors): Flow<TootDetails> {
  return combine(
    comment.countFlow,
    favorite.isEnabledFlow,
    favorite.countFlow,
    reblog.isEnabledFlow,
    reblog.countFlow
  ) { commentCount, isFavorite, favoriteCount, isReblogged, reblogCount ->
    TootDetails(
      id,
      author.avatarURL,
      author.name,
      author.account,
      content.text.toAnnotatedString(colors),
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

/** Converts this [Toot] into [TootDetails]. */
@Composable
internal fun Toot.toTootDetails(): TootDetails {
  val commentCount by comment.countFlow.collectAsState()
  val isFavorite by favorite.isEnabledFlow.collectAsState()
  val favoriteCount by favorite.countFlow.collectAsState()
  val isReblogged by reblog.isEnabledFlow.collectAsState()
  val reblogCount by reblog.countFlow.collectAsState()
  return TootDetails(
    id,
    author.avatarURL,
    author.name,
    author.account,
    content.text.toAnnotatedString(OrcaTheme.colors),
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
