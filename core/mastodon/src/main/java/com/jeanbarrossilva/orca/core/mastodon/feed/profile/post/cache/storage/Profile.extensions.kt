package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author

/** Converts this [Profile] into an [Author]. */
internal fun Profile.toAuthor(): Author {
  return Author(id, avatarLoader, name, account, profileURL = url)
}
