package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.type.Mention

/**
 * Converts this [Style] into an [HttpStyleEntity].
 *
 * @param tootID ID of the [Toot] to which this [Style] belongs.
 **/
internal fun Style.toHttpStyleEntity(tootID: String): HttpStyleEntity {
    val url = if (this is Mention) url.toString() else null
    return HttpStyleEntity(id = 0, tootID, indices.first, indices.last, url)
}
