package com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** A sample [Toot]. **/
val Toot.Companion.sample
    get() = object : Toot() {
        override val id = UUID.randomUUID().toString()
        override val author = Author.sample
        override val content = "<p>This is a <b>sample</b> <i>toot</i> that has the sole purpose " +
            "of allowing one to see how it would look like in <u>Mastodonte</u>.</p>"
        override val publicationDateTime = ZonedDateTime.of(
            /*year =*/ 2003,
            /*month =*/ 10,
            /*dayOfMonth =*/ 8,
            /*hour =*/ 8,
            /*minute =*/ 0,
            /*second =*/ 0,
            /*nanoOfSecond =*/ 0,
            /*zone =*/ ZoneId.of("GMT-3")
        )
    }

/**
 * Sample [Toot]s.
 **/
val Toot.Companion.samples
    get() = List(32) { sample }
