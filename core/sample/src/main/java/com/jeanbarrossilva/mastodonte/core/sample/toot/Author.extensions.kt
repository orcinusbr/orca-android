package com.jeanbarrossilva.mastodonte.core.sample.toot

import com.jeanbarrossilva.mastodonte.core.toot.Author
import com.jeanbarrossilva.mastodonte.core.toot.at
import java.net.URL
import java.util.UUID

/** [Author] returned by [sample]'s getter. **/
private val sampleAuthor = Author(
    "${UUID.randomUUID()}",
    avatarURL = URL(
        "https://en.gravatar.com/userimage/153558542/08942ba9443ce68bf66345a2e6db656e.png"
    ),
    name = "Jean Silva",
    account = "jeanbarrossilva" at "mastodon.social",
    profileURL = URL("https://mastodon.social/@jeanbarrossilva")
)

/** A sample [Author]. **/
val Author.Companion.sample
    get() = sampleAuthor
