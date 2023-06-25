package com.jeanbarrossilva.mastodonte.core.inmemory.profile

import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.util.UUID

/** A sample [Toot]. **/
val Toot.Companion.sample
    get() = object : Toot() {
        override val id = UUID.randomUUID().toString()
        override val author = Author.sample
        override val content = "<p>This is a <b>sample</b> <i>toot</i> that has the sole purpose " +
            "of allowing one to see how it would look like in <u>Mastodonte</u>.</p>"
    }

/**
 * Sample [Toot]s.
 **/
val Toot.Companion.samples
    get() = List(32) { sample }
