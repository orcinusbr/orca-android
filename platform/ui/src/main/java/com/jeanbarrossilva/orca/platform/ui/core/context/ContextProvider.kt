package com.jeanbarrossilva.orca.platform.ui.core.context

import android.content.Context

/** Provides a [Context] through [provide]. */
fun interface ContextProvider {
  /** Provides a [Context]. */
  fun provide(): Context
}
