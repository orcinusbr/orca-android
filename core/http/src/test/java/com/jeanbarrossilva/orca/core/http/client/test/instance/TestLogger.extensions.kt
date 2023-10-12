package com.jeanbarrossilva.orca.core.http.client.test.instance

import com.jeanbarrossilva.orca.core.http.client.Logger

/** [Logger] returned by [test]. */
private val testLogger =
  object : Logger() {
    override fun onInfo(info: String) {}

    override fun onError(error: String) {}
  }

/** A no-op [Logger]. */
internal val Logger.Companion.test
  get() = testLogger
