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

package br.com.orcinus.orca.std.packageinternal.lint.detection

import br.com.orcinus.orca.std.packageinternal.PackageInternal
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.UastLintUtils.Companion.tryResolveUDeclaration
import com.intellij.psi.PsiClass
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UDeclaration
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.getQualifiedChain

/**
 * Returns only the [UExpression]s whose resolved [UDeclaration]s have been marked as
 * package-internal, denoting that accesses from packages that aren't the ones in which the
 * [UDeclaration]s are should be reported.
 *
 * @param context [JavaContext] for finding the [PsiClass]es of [UAnnotation]s with which ones that
 *   might have [PackageInternal] applied to it, allowing for improper accesses to be propagated
 *   with [UAnnotation]s other than [PackageInternal] itself.
 * @see tryResolveUDeclaration
 * @see UDeclaration.isPackageInternal
 */
internal fun UExpression.filterIsResolvedToDeclarationMarkedAsPackageInternal(
  context: JavaContext
): List<UExpression> {
  return getQualifiedChain().filter {
    it.tryResolveUDeclaration()?.isPackageInternal(context) ?: false
  }
}
