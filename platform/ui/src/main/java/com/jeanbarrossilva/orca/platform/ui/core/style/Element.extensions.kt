/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
