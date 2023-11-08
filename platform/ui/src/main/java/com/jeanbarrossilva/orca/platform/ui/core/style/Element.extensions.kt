package com.jeanbarrossilva.orca.platform.ui.core.style

import org.jsoup.nodes.Element
import org.jsoup.nodes.Node

/**
 * Removes this [Element] and appends the child [Node]s that existed prior to its removal to its
 * parent.
 *
 * @see Element.childNodes
 * @see Element.parent
 */
internal fun Element.pop() {
  val parent = parent() ?: return
  val childNodes = childNodes()
  remove()
  parent.appendChildren(childNodes)
}
