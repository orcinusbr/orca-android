package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString

/** Converts this [Toot] into a [TootPreview]. **/
fun Toot.toTootPreview(): TootPreview {
    val body = HtmlAnnotatedString(content)
    return TootPreview(
        author.avatarURL,
        author.name,
        author.account,
        body,
        publicationDateTime,
        commentCount,
        isFavorite,
        favoriteCount,
        isReblogged,
        reblogCount
    )
}
