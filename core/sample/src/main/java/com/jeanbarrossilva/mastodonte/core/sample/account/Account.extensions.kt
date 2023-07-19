package com.jeanbarrossilva.mastodonte.core.sample.account

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.account.at

/** [Account] returned by [sample]. **/
private val sampleAccount = "jeanbarrossilva" at "mastodon.social"

/** A sample [Account]. **/
val Account.Companion.sample
    get() = sampleAccount
