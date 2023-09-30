package com.jeanbarrossilva.orca.core.http.feed.profile.toot.test

import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus

/** Sample [HttpToot]. **/
internal val HttpToot.Companion.sample
    get() = HttpStatus.sample.toToot() as HttpToot
