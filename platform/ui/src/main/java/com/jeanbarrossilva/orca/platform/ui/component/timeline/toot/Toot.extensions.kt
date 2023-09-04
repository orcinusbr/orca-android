package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/** Converts this [Toot] into a [TootPreview]. **/
@Composable
fun Toot.toTootPreview(): TootPreview {
    return toTootPreview(OrcaTheme.colors)
}

/**
 * Converts this [Toot] into a [TootPreview].
 *
 * @param colors [Colors] by which the resulting [TootPreview]'s [TootPreview.body] can be colored.
 **/
fun Toot.toTootPreview(colors: Colors): TootPreview {
    val body = content.toAnnotatedString(colors)
    return TootPreview(
        id,
        author.avatarURL,
        author.name,
        author.account,
        body,
        publicationDateTime,
        commentCount,
        isFavorite,
        favoriteCount,
        isReblogged,
        reblogCount,
        url
    )
}
