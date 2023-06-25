package com.jeanbarrossilva.mastodonte.app

import com.ramcosta.composedestinations.spec.DestinationSpec

/**
 * Whether [other] is a child of this [DestinationSpec].
 *
 * @param other [DestinationSpec] whose kinship to the receiver one will be checked.
 **/
internal operator fun DestinationSpec<*>.contains(other: DestinationSpec<*>?): Boolean {
    return other != null && other.route.startsWith(route)
}
