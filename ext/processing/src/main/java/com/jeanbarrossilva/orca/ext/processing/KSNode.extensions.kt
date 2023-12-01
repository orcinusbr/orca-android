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

package com.jeanbarrossilva.orca.ext.processing

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode

/**
 * Requires the [KSFile] in which this [KSNode] is or throws an [IllegalStateException] if it
 * doesn't have one.
 *
 * @throws IllegalStateException If this [KSNode] isn't part of a [KSFile].
 */
fun KSNode.requireContainingFile(): KSFile {
  return containingFile ?: throw IllegalStateException("$this doesn't have a containing KSFile.")
}
