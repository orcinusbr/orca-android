package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.platform.ui.html.HtmlAnnotatedString

/** Converts this [Toot] into a [TootPreview]. **/
fun Toot.toTootPreview(): TootPreview {
    val body = HtmlAnnotatedString(content)
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
