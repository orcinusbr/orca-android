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
import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles

/** [TestFile] in which a [PackageProtected]-like [Annotation] is declared. */
internal val TestFiles.packageProtectedAnnotation
  get() =
    kotlin(
        """
          package br.com.orcinus.orca.std.visibility

          annotation class PackageProtected(val message: String = "${PackageProtected.DEFAULT_MESSAGE}")
        """
      )
      .indented()

/**
 * [TestFile] in which an [Annotation] that is annotated with the [PackageProtected]-like one from
 * [TestFiles.packageProtectedAnnotation] is declared.
 */
internal val TestFiles.packageProtectedAnnotatedAnnotation
  get() =
    kotlin(
        """
          package br.com.orcinus.orca.std.visibility.lint.detection.test

          import br.com.orcinus.orca.std.visibility.PackageProtected

          @PackageProtected
          internal annotation class PackageProtectedApi
        """
      )
      .indented()

/**
 * Essentially the same [TestFile] as [TestFiles.packageProtectedAnnotatedAnnotation], but with the
 * declared [Annotation] having an alternate message for when references from outside packages are
 * found.
 */
internal val TestFiles.packageProtectedAnnotatedAnnotationWithAlternateMessage
  get() =
    kotlin(
        """
          package br.com.orcinus.orca.std.visibility.lint.detection.test

          import br.com.orcinus.orca.std.visibility.PackageProtected

          @PackageProtected(message = ":P")
          internal annotation class PackageProtectedApi
        """
      )
      .indented()
