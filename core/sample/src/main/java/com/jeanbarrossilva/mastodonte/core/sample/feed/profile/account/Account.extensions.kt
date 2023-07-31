package com.jeanbarrossilva.mastodonte.core.sample.feed.profile.account

import com.jeanbarrossilva.mastodonte.core.feed.profile.account.Account
import com.jeanbarrossilva.mastodonte.core.feed.profile.account.at

/** [Account] returned by [sample]. **/
private val sampleAccount = "jeanbarrossilva" at "mastodon.social"

/** A sample [Account]. **/
val Account.Companion.sample
    get() = sampleAccount
