package com.jeanbarrossilva.orca.core.instance.domain

import java.net.URL

/** Returns whether this [URL] is of a resource that belongs to the [domain]. */
internal fun URL.isOfResourceFrom(domain: Domain): Boolean {
  return host == domain.toString() && path.isNotEmpty()
}
