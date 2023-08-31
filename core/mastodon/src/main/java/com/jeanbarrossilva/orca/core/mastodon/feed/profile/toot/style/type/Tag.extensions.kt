package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.style.type

import kotlinx.html.Tag

/** `translate` attribute of this [Tag]. **/
internal var Tag.translate: Boolean?
    get() = attributes["translate"]?.let { it != "no" }
    set(isTranslatable) {
        isTranslatable?.let {
            attributes["translate"] = if (it) "yes" else "no"
        }
    }
