package com.jeanbarrossilva.mastodonte.core.sample.feed.profile.toot

import com.jeanbarrossilva.mastodonte.core.feed.profile.account.Account
import com.jeanbarrossilva.mastodonte.core.feed.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.account.sample
import java.net.URL

/** [Author] returned by [sample]'s getter. **/
private val sampleAuthor = Author(
    "422924",
    avatarURL = URL(
        "https://en.gravatar.com/userimage/153558542/08942ba9443ce68bf66345a2e6db656e.png"
    ),
    name = "Jean Silva",
    Account.sample,
    profileURL = URL("https://mastodon.social/@jeanbarrossilva")
)

/** A sample [Author]. **/
val Author.Companion.sample
    get() = sampleAuthor
