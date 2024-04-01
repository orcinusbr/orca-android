/*
 * Copyright © 2023–2024 Orcinus
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
