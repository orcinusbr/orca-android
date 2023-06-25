package com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.at
import java.net.URL
import java.util.UUID

/** [Sample][sample] [Author]'s ID. **/
private val sampleID = UUID.randomUUID().toString()

/** A sample [Author]. **/
val Author.Companion.sample
    get() = Author(
        sampleID,
        avatarURL = URL(
            "https://en.gravatar.com/userimage/153558542/08942ba9443ce68bf66345a2e6db656e.png"
        ),
        name = "Jean Silva",
        account = "jeanbarrossilva" at "mastodon.social",
        profileURL = URL("https://mastodon.social/@jeanbarrossilva")
    )
