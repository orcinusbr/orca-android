package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author

/** Converts this [Profile] into an [Author]. **/
internal fun Profile.toAuthor(): Author {
    return Author(id, avatarURL, name, account, profileURL = url)
}
