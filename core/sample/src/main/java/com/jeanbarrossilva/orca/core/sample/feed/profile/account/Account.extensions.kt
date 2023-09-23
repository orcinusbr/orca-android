package com.jeanbarrossilva.orca.core.sample.feed.profile.account

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.account.at
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample

/** [Account] returned by [sample]. **/
private val sampleAccount = "jeanbarrossilva" at Domain.sample.toString()

/** A sample [Account]. **/
val Account.Companion.sample
    get() = sampleAccount
