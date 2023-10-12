package com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication

/** Indicates that duplicate navigation is allowed. */
fun allowingDuplication(): Duplication {
  return Duplication.Allowed
}

/** Indicates that duplicate navigation is disallowed. */
fun disallowingDuplication(): Duplication {
  return Duplication.Disallowed
}
