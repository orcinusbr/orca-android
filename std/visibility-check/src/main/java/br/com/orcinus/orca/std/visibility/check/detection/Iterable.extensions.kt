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

import br.com.orcinus.orca.std.visibility.PackageProtected
import com.android.tools.lint.detector.api.UastLintUtils.Companion.tryResolveUDeclaration
import com.android.utils.associateWithNotNull
import com.android.utils.mapValuesNotNull
import org.jetbrains.uast.UDeclaration
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.getContainingDeclaration

/**
 * Performs the given [action] on each of the [UExpression]s whose resolved [UDeclaration] has been
 * marked as package-protected, denoting that references from packages that aren't the ones in which
 * the [UDeclaration]s are should be reported.
 *
 * @param action Operation to be run on the [UExpression]s that both contain references to
 *   package-protected structures and are from packages that are unrelated to those of the
 *   structures, alongside the custom message that has been specified by the [PackageProtected]
 *   annotation with which these structures' [UDeclaration]s have been annotated.
 * @see tryResolveUDeclaration
 * @see UDeclaration.findPackageProtectedAnnotation
 */
internal fun Iterable<UExpression>
  .withResolvedToDeclarationMarkedAsPackageProtectedReferencedFromOutsidePackage(
  action: (expression: UExpression, message: String) -> Unit
) {
  associateWithNotNull { it.tryResolveUDeclaration() }
    .mapValuesNotNull { (expression, resolvedDeclaration) ->
      with({ declaration: UDeclaration ->
        if (expression.isFromPackageOutsideOfThatOf(declaration)) {
          declaration
            .findPackageProtectedAnnotation()
            ?.findAttributeValue(PackageProtected::message.name)
            ?.evaluate()
            ?.toString()
        } else {
          null
        }
      }) {
        invoke(resolvedDeclaration) ?: resolvedDeclaration.getContainingDeclaration()?.let(::invoke)
      }
    }
    .forEach(action)
}
