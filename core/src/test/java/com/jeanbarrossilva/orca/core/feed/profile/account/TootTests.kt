package com.jeanbarrossilva.orca.core.feed.profile.account

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TootTests {
    @Test
    fun convertsIntoBoost() {
        val reblogger = Author.sample.copy(id = "🥸")
        assertEquals(Reblog(Toot.sample, reblogger), Toot.sample.toReblog(reblogger))
    }
}
