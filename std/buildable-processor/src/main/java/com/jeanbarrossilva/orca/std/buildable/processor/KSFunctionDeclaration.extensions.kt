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

package com.jeanbarrossilva.orca.std.buildable.processor

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.jeanbarrossilva.orca.ext.processing.compile
import com.jeanbarrossilva.orca.ext.processing.requireContainingFile
import org.jetbrains.kotlin.psi.KtNamedFunction

/** Body of this [KSFunctionDeclaration]. */
internal val KSFunctionDeclaration.body: String?
  get() {
    return (findOverridee() ?: this)
      .requireContainingFile()
      .compile()
      ?.flattenChildren()
      ?.filterIsInstance<KtNamedFunction>()
      ?.first { it.name == simpleName.asString() }
      ?.bodyExpression
      ?.text
      ?.replaceLast("return ", "")
  }
