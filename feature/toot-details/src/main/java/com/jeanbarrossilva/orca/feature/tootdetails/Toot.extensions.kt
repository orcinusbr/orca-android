package com.jeanbarrossilva.orca.feature.tootdetails

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.platform.ui.html.HtmlAnnotatedString

/** Converts this core [Toot] into [TootDetails]. **/
internal fun Toot.toTootDetails(): TootDetails {
    return TootDetails(
        id,
        author.avatarURL,
        author.name,
        author.account,
        body = HtmlAnnotatedString(content),
        publicationDateTime,
        commentCount,
        isFavorite,
        favoriteCount,
        isReblogged,
        reblogCount,
        url
    )
}
