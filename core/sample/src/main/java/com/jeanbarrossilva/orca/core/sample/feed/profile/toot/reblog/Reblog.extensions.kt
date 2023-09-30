package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.reblog

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.rambo
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.flow.flowOf

/** [Reblog] returned by [sample]. **/
private val sampleReblog = Reblog(
    "${UUID.randomUUID()}",
    Author.rambo,
    reblogger = Author.sample,
    Content.from(
        StyledString(
            "Programming life hack. Looking for real-world examples of how an API is used? " +
                "Search for code on GitHub like so: “APINameHere path:*.extension”. Practical " +
                "example for a MusicKit API in Swift: " +
                "“MusicCatalogResourceRequest extension:*.swift”. I can usually find lots of " +
                "examples to help me get things going without having to read the entire " +
                "documentation for a given API."
        )
    ) { null },
    publicationDateTime = ZonedDateTime.of(2023, 8, 16, 16, 48, 43, 384, ZoneId.of("GMT-3")),
    commentCount = 4,
    isFavorite = false,
    favoriteCount = 151,
    isReblogged = true,
    reblogCount = 34,
    url = URL("https://mastodon.social/@_inside/110900315644335855"),
    getComments = { flowOf(emptyList()) }
)

/** Sample [Reblog]. **/
val Reblog.Companion.sample
    get() = sampleReblog
