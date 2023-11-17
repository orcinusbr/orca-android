package com.jeanbarrossilva.orca.core.mastodon.auth.authorization.selectable

import androidx.annotation.Discouraged

/**
 * Holds a [value] that can be selected.
 *
 * @param value Value that's selected or unselected, according to [isSelected].
 * @param isSelected Whether [value] is considered to be selected.
 */
internal data class Selectable<T>
@Discouraged("Use the `select` or `unselect` extension functions instead.")
constructor(val value: T, val isSelected: Boolean)
