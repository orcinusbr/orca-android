package com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot

/** [Toot] whose operations are performed in memory. **/
data class InMemoryToot(
    override val id: String,
    override val author: Author,
    override val content: String
) : Toot()
