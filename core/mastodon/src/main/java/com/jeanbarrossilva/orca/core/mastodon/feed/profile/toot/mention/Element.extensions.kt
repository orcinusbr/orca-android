package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.mention

import com.jeanbarrossilva.orca.core.feed.profile.toot.mention.Mention
import org.jsoup.nodes.Element

/** Whether this [Element] is that of a [Mention]. **/
internal val Element.isMention
    get() = tagName() == "a" && attr("class") == "u-url mention"
