package com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString

/** Converts this [Toot] into a [TootPreview]. **/
fun Toot.toTootPreview(): TootPreview {
    val body = HtmlAnnotatedString(content)
    return TootPreview(
        author.avatarURL,
        author.name,
        username = "@${author.account.username}",
        timeSincePublication = publicationDateTime.relative,
        body,
        commentCount.formatted,
        favoriteCount.formatted,
        reblogCount.formatted
    )
}
