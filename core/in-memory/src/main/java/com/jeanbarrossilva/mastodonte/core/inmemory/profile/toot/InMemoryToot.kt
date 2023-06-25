package com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.time.ZonedDateTime

/** [Toot] whose operations are performed in memory. **/
data class InMemoryToot(
    override val id: String,
    override val author: Author,
    override val content: String,
    override val publicationDateTime: ZonedDateTime,
    override val commentCount: Int,
    override val favoriteCount: Int,
    override val reblogCount: Int
) : Toot()
