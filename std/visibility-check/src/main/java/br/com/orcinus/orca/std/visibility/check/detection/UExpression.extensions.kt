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

import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UDeclaration
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.getContainingUFile

/**
 * Returns whether this [UExpression] is in a package from which the contained structure shouldn't
 * be referenced.
 *
 * @param referenceDeclaration [UDeclaration] of the structure referenced by the receiver
 *   [UExpression].
 * @param annotationDeclaration [UDeclaration] of the [UAnnotation].
 * @param isAnnotationInherited Whether the [referenceDeclaration] is annotated by a [UAnnotation]
 *   that "inherits" the behavior of the one by which package-protected visibility is defined
 *   instead of being annotated with the latter directly.
 */
internal fun UExpression.isFromOutsidePackage(
  referenceDeclaration: UDeclaration,
  annotationDeclaration: UDeclaration,
  isAnnotationInherited: Boolean
): Boolean {
  return !isAnnotationInherited && isFromPackageOutsideOfThatOf(referenceDeclaration) ||
    isFromPackageOutsideOfThatOf(annotationDeclaration)
}

/**
 * Returns whether this [UExpression] is in a package that isn't that of the [declaration].
 *
 * @param declaration [UDeclaration] whose package will be compared to this [UExpression]'s.
 */
private fun UExpression.isFromPackageOutsideOfThatOf(declaration: UDeclaration): Boolean {
  val expressionPackageName = getContainingUFile()?.packageName ?: return false
  val declarationPackageName = declaration.getContainingUFile()?.packageName ?: return false
  return !expressionPackageName.startsWith(declarationPackageName)
}
