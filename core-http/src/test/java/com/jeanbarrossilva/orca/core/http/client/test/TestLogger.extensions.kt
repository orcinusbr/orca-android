package com.jeanbarrossilva.orca.core.http.client.test

import com.jeanbarrossilva.orca.core.http.client.Logger

/** [Logger] returned by [test]. **/
private val testLogger = object : Logger {
    override fun info(info: String) {
    }

    override fun error(error: String) {
    }
}

/** A no-op [Logger]. **/
internal val Logger.Companion.test
    get() = testLogger
