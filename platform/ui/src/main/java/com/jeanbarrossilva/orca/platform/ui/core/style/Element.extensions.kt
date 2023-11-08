package com.jeanbarrossilva.orca.platform.ui.core.style

import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

/**
 * Removes this [Element] and returns the child [Node]s that existed prior to its removal.
 *
 * @see Element.childNodes
 */
internal fun Element.pop(): List<Node> {
  return childNodes().also { remove() }
}
