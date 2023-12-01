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

import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Visibility

/** Whether this [KSDeclaration] is abstract. */
internal val KSDeclaration.isAbstract
  get() =
    when (this) {
      is KSFunctionDeclaration -> isAbstract
      is KSPropertyDeclaration -> isAbstract()
      else -> false
    }

/** [Visibility] of the root [KSDeclaration]. */
internal val KSDeclaration.rootDeclarationVisibility: Visibility
  get() {
    var rootDeclaration = this
    while (rootDeclaration.parentDeclaration != null) {
      rootDeclaration = rootDeclaration.parentDeclaration!!
    }
    return rootDeclaration.getVisibility()
  }
