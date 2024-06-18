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

import com.android.tools.lint.detector.api.UastLintUtils.Companion.tryResolveUDeclaration
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UDeclaration

/**
 * Qualified name of the annotation for changing the visibility of a structure to package-protected.
 */
internal const val PACKAGE_PROTECTED_ANNOTATION_NAME =
  "br.com.orcinus.orca.std.visibility.PackageProtected"

/**
 * Obtains the [UAnnotation] that makes a structure be package-protected if this [UDeclaration] is
 * of one that has been annotated with it or with another [UAnnotation] that "inherits" that one's
 * behavior, denoting that references from a package that isn't the one in which either it or this
 * [UDeclaration] is should be reported.
 */
internal fun UDeclaration.findPackageProtectedAnnotation(): UAnnotation? {
  return findAnnotation(PACKAGE_PROTECTED_ANNOTATION_NAME)
    ?: uAnnotations.find {
      it.tryResolveUDeclaration()?.hasAnnotation(PACKAGE_PROTECTED_ANNOTATION_NAME) ?: false
    }
}
