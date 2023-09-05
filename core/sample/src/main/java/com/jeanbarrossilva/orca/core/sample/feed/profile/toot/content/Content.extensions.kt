package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.std.styledstring.buildStyledString

/** [Content] that's returned by [sample]'s getter. **/
private val sampleContent = Content.from(
    buildStyledString {
        +"This is a "
        bold { +"sample" }
        italic { +"toot" }
        +"that has the sole purpose of allowing one to see how it would look like in Orca."
        +"\n".repeat(2)
        +Highlight.sample.url.toString()
    }
) {
    Headline.sample
}

/** Sample [Content]. **/
val Content.Companion.sample
    get() = sampleContent
