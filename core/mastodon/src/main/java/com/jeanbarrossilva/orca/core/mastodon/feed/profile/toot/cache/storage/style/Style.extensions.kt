package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.Style
import com.jeanbarrossilva.orca.core.feed.profile.toot.style.type.Mention

/**
 * Converts this [Style] into a [StyleEntity].
 *
 * @param tootID ID of the [Toot] to which this [Style] belongs.
 **/
internal fun Style.toMentionEntity(tootID: String): StyleEntity {
    val url = if (this is Mention) url.toString() else null
    return StyleEntity(id = 0, tootID, indices.first, indices.last, url)
}
