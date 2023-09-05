package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import java.net.URL

/** [Highlight] that's returned by [sample]'s getter. **/
private val sampleHighlight = Highlight(Headline.sample, URL("https://mastodon.social"))

/** Sample [Highlight]. **/
val Highlight.Companion.sample
    get() = sampleHighlight
