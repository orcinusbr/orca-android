package com.jeanbarrossilva.orca.core.http.test

import com.jeanbarrossilva.orca.core.http.Logger

/** A no-op [Logger]. **/
internal object TestLogger : Logger {
    override fun info(info: String) {
    }

    override fun error(error: String) {
    }
}
