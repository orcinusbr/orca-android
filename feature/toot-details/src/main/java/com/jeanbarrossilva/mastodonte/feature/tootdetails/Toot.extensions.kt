package com.jeanbarrossilva.mastodonte.feature.tootdetails

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
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
        reblogCount,
        url
    )
}
