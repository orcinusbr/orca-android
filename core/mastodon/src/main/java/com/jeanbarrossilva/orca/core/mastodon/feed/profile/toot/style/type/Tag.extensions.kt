package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import kotlinx.html.Tag

/**
 * Sets the `translate` attribute to this [Tag].
 *
 * @param isTranslatable Whether its contents are translatable.
 **/
internal fun Tag.translate(isTranslatable: Boolean) {
    attributes["translate"] = if (isTranslatable) "yes" else "no"
}
