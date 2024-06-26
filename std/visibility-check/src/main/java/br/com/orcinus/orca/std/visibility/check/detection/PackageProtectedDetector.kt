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

import com.android.tools.lint.detector.api.AnnotationInfo
import com.android.tools.lint.detector.api.AnnotationUsageInfo
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.getQualifiedChain

/**
 * [Detector] that reports accesses to structures marked as package-protected that have been made
 * from a package that is neither the one in which they have been declared nor one of its children
 * (i. e., a class marked as package-protected declared at `br.com.orcinus.orca.core` can be
 * accessed from `br.com.orcinus.orca.core` and `br.com.orcinus.orca.core.sample`, but shouldn't be
 * accessed from `br.com.orcinus.orca.app`).
 */
internal class PackageProtectedDetector : Detector(), SourceCodeScanner {
  override fun visitAnnotationUsage(
    context: JavaContext,
    element: UElement,
    annotationInfo: AnnotationInfo,
    usageInfo: AnnotationUsageInfo
  ) {
    if (element is UExpression) {
      reportExpressionsResolvedToDeclarationsMarkedAsPackageProtectedReferencedFromOutsidePackage(
        context,
        element
      )
    }
  }

  override fun applicableAnnotations(): List<String> {
    return listOfNotNull(PACKAGE_PROTECTED_ANNOTATION_NAME)
  }

  override fun getApplicableUastTypes(): List<Class<UExpression>> {
    return listOf(UExpression::class.java)
  }

  /**
   * Reports references to package-protected structures from outside the package in which they have
   * been declared or its children in each of the qualified [UExpression]s within the [expression].
   *
   * @param context [JavaContext] through which qualified [UExpression]s that refer to
   *   package-protected structures from outside packages will be obtained from the [expression].
   * @param expression [UExpression] to be checked.
   * @see Iterable.withResolvedToDeclarationMarkedAsPackageProtectedReferencedFromOutsidePackage
   */
  private fun reportExpressionsResolvedToDeclarationsMarkedAsPackageProtectedReferencedFromOutsidePackage(
    context: JavaContext,
    expression: UExpression
  ) {
    expression
      .getQualifiedChain()
      .withResolvedToDeclarationMarkedAsPackageProtectedReferencedFromOutsidePackage {
        qualifiedExpression,
        message ->
        context.report(Incident(context, issue).at(qualifiedExpression).message(message))
      }
  }

  companion object {
    /** [Issue] that informs of improper accesses to package-protected structures. */
    val issue =
      Issue.create(
        id = "PackageProtected",
        briefDescription = "Access to package-protected structure",
        explanation =
          "Structures marked as package-protected should only be accessed from the package in " +
            "which they have been declared or one of its children.",
        Category.CORRECTNESS,
        priority = 5,
        Severity.ERROR,
        Implementation(PackageProtectedDetector::class.java, Scope.JAVA_FILE_SCOPE)
      )
  }
}
