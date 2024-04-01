/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.composite.timeline.text

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
