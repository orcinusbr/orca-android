package com.jeanbarrossilva.orca.core.http.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.test.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import org.junit.Assert.assertEquals
import org.junit.Test

internal class HttpStatusTests {
    @Test
    fun convertsIntoReblog() {
        assertEquals(
            Reblog(HttpToot.sample.copy(id = "🐣"), Author.sample),
            HttpStatus.sample.copy(reblog = HttpStatus.sample.copy(id = "🐣")).toToot()
        )
    }
}
