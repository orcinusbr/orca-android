package com.jeanbarrossilva.mastodonte.feature.tootdetails

import com.jeanbarrossilva.mastodonte.core.feed.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString

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
