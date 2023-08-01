package com.jeanbarrossilva.orca.core.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.Profile

/** Converts this [Profile] into a [ProfileSearchResult]. **/
fun Profile.toProfileSearchResult(): ProfileSearchResult {
    return ProfileSearchResult(id, account, avatarURL, name, url)
}
