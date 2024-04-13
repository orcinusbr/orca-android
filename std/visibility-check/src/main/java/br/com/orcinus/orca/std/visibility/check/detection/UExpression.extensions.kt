/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.std.visibility.check.detection

import org.jetbrains.uast.UDeclaration
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.getContainingUFile

/**
 * Returns whether this [UExpression] has been created in a package that isn't that in which the
 * [declaration] is or one of its children.
 *
 * @param declaration [UDeclaration] that is marked as package-protected and whose package will be
 *   compared to that in which this [UExpression] is.
 */
internal fun UExpression.isFromPackageOutsideOfThatOf(declaration: UDeclaration): Boolean {
  val expressionFile = getContainingUFile() ?: return false
  val declarationFile = declaration.getContainingUFile() ?: return false
  return !expressionFile.packageName.startsWith(declarationFile.packageName)
}
