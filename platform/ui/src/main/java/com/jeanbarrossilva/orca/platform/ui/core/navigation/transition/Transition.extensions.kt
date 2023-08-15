package com.jeanbarrossilva.orca.platform.ui.core.navigation.transition

import androidx.fragment.app.FragmentTransaction

/** Creates a close [Transition]. **/
fun closing(): Transition {
    return Transition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
}

/** Creates an open [Transition]. **/
fun opening(): Transition {
    return Transition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
}

/** Creates a sudden [Transition]. **/
fun suddenly(): Transition {
    return Transition(FragmentTransaction.TRANSIT_NONE)
}
