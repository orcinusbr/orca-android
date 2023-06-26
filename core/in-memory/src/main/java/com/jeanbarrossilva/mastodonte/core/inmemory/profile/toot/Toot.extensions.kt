package com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** [sample]'s ID. **/
private val sampleID = UUID.randomUUID().toString()

/** A sample [Toot]. **/
val Toot.Companion.sample
    get() = object : Toot() {
        override val id = sampleID
        override val author = Author.sample
        override val content = "<p>This is a <b>sample</b> <i>toot</i> that has the sole purpose " +
            "of allowing one to see how it would look like in <u>Mastodonte</u>.</p>"
        override val publicationDateTime =
            ZonedDateTime.of(2003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3"))
        override val commentCount = 12
        override val favoriteCount = 56
        override val reblogCount = 2

        @Suppress("SpellCheckingInspection")
        override val url = URL("https://mastodon.social/@christianselig/110492858891694580")

        override suspend fun getComments(page: Int): Flow<List<Toot>> {
            return flowOf(emptyList())
        }
    }

/**
 * Sample [Toot]s.
 **/
val Toot.Companion.samples
    get() = List(32) { sample }
