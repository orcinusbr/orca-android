package com.jeanbarrossilva.orca.core.sample.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample

/** A sample [ProfileSearchResult]. **/
val ProfileSearchResult.Companion.sample
    get() = ProfileSearchResult(
        Author.sample.id,
        Author.sample.account,
        Author.sample.avatarURL,
        Author.sample.name,
        Author.sample.profileURL
    )
