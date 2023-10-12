package com.jeanbarrossilva.orca.platform.ui.core.navigation.transition

import androidx.fragment.app.FragmentTransaction

/**
 * Indicates how navigation from one destination to another will transition.
 *
 * @param value [FragmentTransaction]-related value that's equivalent to this [Transition].
 */
@JvmInline
value class Transition internal constructor(internal val value: Int) {
  init {
    require(
      value == FragmentTransaction.TRANSIT_NONE ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_FADE ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_OPEN ||
        value == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
    ) {
      "Unknown value: $value."
    }
  }
}
