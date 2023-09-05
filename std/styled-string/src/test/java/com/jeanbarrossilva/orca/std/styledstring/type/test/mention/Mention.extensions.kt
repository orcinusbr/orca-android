package com.jeanbarrossilva.orca.std.styledstring.type.test.mention

import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.type.Mention
import java.net.URL

/**
 * Sample [URL] to create mentions with.
 *
 * @see StyledString.Builder.mention
 * @see Mention
 **/
internal val Mention.Companion.url
    get() = URL("https://mastodon.social/@jeanbarrossilva")

/**
 * Sample username to mention.
 *
 * @see StyledString.Builder.mention
 * @see Mention
 **/
internal val Mention.Companion.username
    get() = "jeanbarrossilva"
