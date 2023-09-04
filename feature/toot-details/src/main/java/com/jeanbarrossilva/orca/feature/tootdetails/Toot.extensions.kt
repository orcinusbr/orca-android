package com.jeanbarrossilva.orca.feature.tootdetails

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString

/**
 * Converts this core [Toot] into [TootDetails].
 *
 * @param colors [Colors] by which the resulting [TootDetails]' [TootDetails.body] can be colored.
 **/
internal fun Toot.toTootDetails(colors: Colors): TootDetails {
    return TootDetails(
        id,
        author.avatarURL,
        author.name,
        author.account,
        body = content.toAnnotatedString(colors),
        publicationDateTime,
        commentCount,
        isFavorite,
        favoriteCount,
        isReblogged,
        reblogCount,
        url
    )
}
