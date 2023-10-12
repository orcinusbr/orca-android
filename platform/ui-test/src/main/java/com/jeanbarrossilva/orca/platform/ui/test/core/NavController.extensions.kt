package com.jeanbarrossilva.orca.platform.ui.test.core

import androidx.navigation.NavController

/** Whether this [NavController] has a [graph][NavController.graph]. */
internal val NavController.hasNavGraph
  get() =
    try {
      graph
      true
    } catch (_: IllegalStateException) {
      false
    }
