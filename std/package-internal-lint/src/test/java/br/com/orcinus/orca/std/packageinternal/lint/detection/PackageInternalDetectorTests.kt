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

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import kotlin.test.Test

internal class PackageInternalDetectorTests {
  private val lintTask = TestLintTask.lint().allowMissingSdk().issues(PackageInternalDetector.issue)

  @Test
  fun reportsOnReferenceFromOutsidePackageToClassMarkedAsPackageInternalFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageInternalAnnotation,
        TestFiles.packageInternalAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.packageinternal.lint.detection.test

              import br.com.orcinus.orca.std.packageinternal.PackageInternal

              @PackageInternalApi
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.packageinternal.lint.detection.test.Api

              private object Consumer {
                init {
                  Api()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect(
        """
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageInternalDetector.MESSAGE} [${PackageInternalDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToPackageInternalClass() {
    lintTask
      .files(
        TestFiles.packageInternalAnnotation,
        TestFiles.kotlin(
            """
            package br.com.orcinus.orca.std.packageinternal.lint.detection.test

            import br.com.orcinus.orca.std.packageinternal.PackageInternal

            @PackageInternal
            class Api
          """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.packageinternal.lint.detection.test.Api

              private object Consumer {
                init {
                  Api()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect(
        """
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageInternalDetector.MESSAGE} [${PackageInternalDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToClassConstructorMarkedAsPackageInternalFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageInternalAnnotation,
        TestFiles.packageInternalAnnotatedAnnotation,
        TestFiles.kotlin(
            """
            package br.com.orcinus.orca.std.packageinternal.lint.detection.test

            class Api @PackageInternalApi constructor()
          """
          )
          .indented(),
        TestFiles.kotlin(
            """
            package br.com.orcinus.orca.app

            import br.com.orcinus.orca.std.packageinternal.lint.detection.test.Api

            private object Consumer {
              init {
                Api()
              }
            }
          """
          )
          .indented()
      )
      .run()
      .expect(
        """
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageInternalDetector.MESSAGE} [${PackageInternalDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToPackageInternalClassConstructor() {
    lintTask
      .files(
        TestFiles.packageInternalAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.packageinternal.lint.detection.test

              import br.com.orcinus.orca.std.packageinternal.PackageInternal

              class Api @PackageInternal constructor()
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.packageinternal.lint.detection.test.Api

              private object Consumer {
                init {
                  Api()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect(
        """
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageInternalDetector.MESSAGE} [${PackageInternalDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }
}
