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
import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles

/** [TestFile] in which a [PackageInternal]-like [Annotation] in declared. */
internal val TestFiles.packageInternalAnnotation
  get() =
    kotlin(
        """
          package br.com.orcinus.orca.std.packageinternal

          annotation class PackageInternal
        """
      )
      .indented()

/**
 * [TestFile] in which an [Annotation] that is annotated with the [PackageInternal]-like one from
 * [TestFiles.packageInternalAnnotation] is declared.
 */
internal val TestFiles.packageInternalAnnotatedAnnotation
  get() =
    kotlin(
        """
          package br.com.orcinus.orca.std.packageinternal.lint.detection.test

          import br.com.orcinus.orca.std.packageinternal.PackageInternal

          @PackageInternal
          internal annotation class PackageInternalApi
        """
      )
      .indented()
