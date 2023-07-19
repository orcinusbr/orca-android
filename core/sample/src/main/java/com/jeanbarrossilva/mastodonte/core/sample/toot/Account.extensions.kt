package com.jeanbarrossilva.mastodonte.core.sample.toot

import com.jeanbarrossilva.mastodonte.core.toot.Account
import com.jeanbarrossilva.mastodonte.core.toot.at

/** [Account] returned by [sample]. **/
private val sampleAccount = "jeanbarrossilva" at "mastodon.social"

/** A sample [Account]. **/
val Account.Companion.sample
    get() = sampleAccount
