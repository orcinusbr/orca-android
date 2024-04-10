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

package br.com.orcinus.orca.std.visibility.lint.detection

import br.com.orcinus.orca.std.visibility.PackageProtected
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.getQualifiedChain

/**
 * [Detector] that reports accesses to structures marked as package-protected that have been made
 * from a package that is neither the one in which they have been declared nor one of its
 * derivatives (i. e., a class marked as package-protected declared at `br.com.orcinus.orca.core`
 * can be accessed from `br.com.orcinus.orca.core` and `br.com.orcinus.orca.core.sample`, but
 * shouldn't be accessed from `br.com.orcinus.orca.app`).
 *
 * A structure is considered to have package-protected visibility if it's annotated with
 * [PackageProtected].
 */
internal class PackageProtectedDetector : Detector(), SourceCodeScanner {
  override fun getApplicableUastTypes(): List<Class<out UElement>> {
    return listOf(UCallExpression::class.java)
  }

  override fun createUastHandler(context: JavaContext): UElementHandler {
    return object : UElementHandler() {
      override fun visitCallExpression(node: UCallExpression) {
        reportExpressionsResolvedToDeclarationsMarkedAsPackageProtectedReferencedFromOutsidePackage(
          context,
          node
        )
      }
    }
  }

  /**
   * Reports references to package-protected structures from outside the package in which they have
   * been declared or its derivatives in each of the qualified [UExpression]s within the
   * [expression].
   *
   * @param context [JavaContext] with which [UExpression] that refer to package-private structures
   *   from an outside package will be obtained from the [expression].
   * @param expression [UExpression] to be checked.
   */
  private fun reportExpressionsResolvedToDeclarationsMarkedAsPackageProtectedReferencedFromOutsidePackage(
    context: JavaContext,
    expression: UExpression
  ) {
    Incident(context, issue).message(MESSAGE).let {
      expression
        .getQualifiedChain()
        .filterIsResolvedToDeclarationMarkedAsPackageProtectedReferencedFromOutsidePackage(context)
        .map(it::at)
        .forEach(context::report)
    }
  }

  companion object {
    /**
     * Message that is shown when structures with package-protected visibility are referenced from
     * an outside or non-derivative package.
     */
    const val MESSAGE = "This structure is package-protected."

    /** [Issue] that informs of improper accesses to package-protected structures. */
    val issue =
      Issue.create(
        id = "PackageProtected",
        briefDescription = "Access to package-protected structure",
        explanation =
          "Structures marked as package-protected should only be accessed from the package in " +
            "which they have been declared or one of its derivatives (the child ones, those " +
            "within it).",
        Category.CORRECTNESS,
        priority = 5,
        Severity.ERROR,
        Implementation(PackageProtectedDetector::class.java, Scope.JAVA_FILE_SCOPE)
      )
  }
}
