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

/**
 * [Detector] that reports accesses to structures marked as package-internal that have been made
 * from a package that is neither the one in which they have been declared nor one of its
 * derivatives.
 *
 * A structure is considered to have package-internal visibility if it's annotated with
 * [PackageInternal].
 */
internal class PackageInternalDetector : Detector(), SourceCodeScanner {
  override fun getApplicableUastTypes(): List<Class<out UElement>> {
    return listOf(UCallExpression::class.java)
  }

  override fun inheritAnnotation(annotation: String): Boolean {
    return false
  }

  override fun createUastHandler(context: JavaContext): UElementHandler {
    return object : UElementHandler() {
      override fun visitCallExpression(node: UCallExpression) {
        reportExpressionsResolvedToDeclarationsMarkedAsPackageInternal(context, node)
      }
    }
  }

  private fun reportExpressionsResolvedToDeclarationsMarkedAsPackageInternal(
    context: JavaContext,
    expression: UExpression
  ) {
    expression.filterIsResolvedToDeclarationMarkedAsPackageInternal(context).forEach {
      report(context, it)
    }
  }

  private fun report(context: JavaContext, element: UElement) {
    val incident = Incident(context, issue).at(element).message(MESSAGE)
    context.report(incident)
  }

  companion object {
    /**
     * Message that is shown when structures with package-internal visibility are referenced from an
     * outside or non-derivative package.
     */
    const val MESSAGE = "This structure is package-internal."

    /** [Issue] that informs of improper accesses to package-internal structures. */
    val issue =
      Issue.create(
        id = "PackageInternal",
        briefDescription = "Access to package-internal structure",
        explanation =
          "Structures marked as package-internal should only be accessed from the package in " +
            "which they have been declared or one of its derivatives (the child ones, those " +
            "within it).",
        Category.CORRECTNESS,
        priority = 5,
        Severity.ERROR,
        Implementation(PackageInternalDetector::class.java, Scope.JAVA_FILE_SCOPE)
      )
  }
}
