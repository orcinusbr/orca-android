package com.jeanbarrossilva.orca.core.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable.ToggleableStat
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import java.net.URL
import java.time.ZonedDateTime

/** Local [Toot] that defaults its properties' values to [Toot.Companion.sample]'s. **/
internal class TestToot(
    override val id: String = Toot.sample.id,
    override val author: Author = Toot.sample.author,
    override val content: Content = Toot.sample.content,
    override val publicationDateTime: ZonedDateTime = Toot.sample.publicationDateTime,
    override val comment: Stat<Toot> = Toot.sample.comment,
    override val favorite: ToggleableStat<Profile> = Toot.sample.favorite,
    override val reblog: ToggleableStat<Profile> = Toot.sample.reblog,
    override val url: URL = Toot.sample.url
) : Toot()
