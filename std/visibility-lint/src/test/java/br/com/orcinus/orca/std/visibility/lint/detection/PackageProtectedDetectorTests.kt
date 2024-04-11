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
import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import kotlin.test.Test

internal class PackageProtectedDetectorTests {
  private val lintTask =
    TestLintTask.lint().allowMissingSdk().issues(PackageProtectedDetector.issue)

  @Test
  fun reportsOnReferenceFromOutsidePackageToClassMarkedAsPackageProtectedFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              @PackageProtectedApi
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageProtected.DEFAULT_MESSAGE} [${PackageProtectedDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToClassMarkedAsPackageProtectedFromCustomAnnotationWithAlternateMessage() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotationWithAlternateMessage,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              @PackageProtectedApi
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: :P [${PackageProtectedDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToPackageProtectedClass() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              @PackageProtected
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageProtected.DEFAULT_MESSAGE} [${PackageProtectedDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToPackageProtectedClassWithAlternateMessage() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              @PackageProtected(message = ":P")
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: :P [${PackageProtectedDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun doesNotReportOnReferenceFromSamePackageToClassMarkedAsPackageProtectedFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              @PackageProtectedApi
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
      .expect("No warnings.")
  }

  @Test
  fun doesNotReportOnReferenceFromChildPackageToClassMarkedAsPackageProtectedFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              @PackageProtectedApi
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test.consumer

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
      .expect("No warnings.")
  }

  @Test
  fun doesNotReportOnReferenceFromSamePackageToPackageProtectedClass() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              @PackageProtected
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
      .expect("No warnings.")
  }

  @Test
  fun doesNotReportOnReferenceFromChildPackageToPackageProtectedClass() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              @PackageProtected
              class Api
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test.consumer

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
      .expect("No warnings.")
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToClassConstructorMarkedAsPackageProtectedFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              class Api @PackageProtectedApi constructor()
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageProtected.DEFAULT_MESSAGE} [${PackageProtectedDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToClassConstructorMarkedAsPackageProtectedFromCustomAnnotationWithAlternateMessage() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotationWithAlternateMessage,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              class Api @PackageProtectedApi constructor()
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: :P [${PackageProtectedDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToPackageProtectedClassConstructor() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              class Api @PackageProtected constructor()
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageProtected.DEFAULT_MESSAGE} [${PackageProtectedDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToPackageProtectedClassConstructorWithAlternateMessage() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              class Api @PackageProtected(message = ":P") constructor()
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: :P [${PackageProtectedDetector.issue.id}]
              Api()
              ~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun doesNotReportOnReferenceFromSamePackageToClassConstructorMarkedAsPackageProtectedFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              class Api @PackageProtectedApi constructor()
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
      .expect("No warnings.")
  }

  @Test
  fun doesNotReportOnReferenceFromChildPackageToPackageProtectedClassConstructor() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              class Api @PackageProtected constructor()
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test.consumer

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

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
      .expect("No warnings.")
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToMethodMarkedAsPackageProtectedFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              class Api {
                @PackageProtectedApi
                fun call() {
                }
              }
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

              private object Consumer {
                init {
                  Api().call()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect(
        """
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageProtected.DEFAULT_MESSAGE} [${PackageProtectedDetector.issue.id}]
              Api().call()
              ~~~~~~~~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToMethodMarkedAsPackageProtectedFromCustomAnnotationWithAlternateMessage() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotationWithAlternateMessage,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              class Api {
                @PackageProtectedApi
                fun call() {
                }
              }
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

              private object Consumer {
                init {
                  Api().call()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect(
        """
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: :P [${PackageProtectedDetector.issue.id}]
              Api().call()
              ~~~~~~~~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToPackageProtectedMethod() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              class Api {
                @PackageProtected
                fun call() {
                }
              }
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

              private object Consumer {
                init {
                  Api().call()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect(
        """
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: ${PackageProtected.DEFAULT_MESSAGE} [${PackageProtectedDetector.issue.id}]
              Api().call()
              ~~~~~~~~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun reportsOnReferenceFromOutsidePackageToPackageProtectedMethodWithAlternateMessage() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              class Api {
                @PackageProtected(message = ":P")
                fun call() {
                }
              }
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.app

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

              private object Consumer {
                init {
                  Api().call()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect(
        """
          src/br/com/orcinus/orca/app/Consumer.kt:7: Error: :P [${PackageProtectedDetector.issue.id}]
              Api().call()
              ~~~~~~~~~~~~
          1 errors, 0 warnings
        """
      )
  }

  @Test
  fun doesNotReportOnReferenceFromSamePackageToMethodMarkedAsPackageProtectedFromCustomAnnotation() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.packageProtectedAnnotatedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              class Api {
                @PackageProtectedApi
                fun call() {
                }
              }
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

              private object Consumer {
                init {
                  Api().call()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect("No warnings.")
  }

  @Test
  fun doesNotReportOnReferenceFromChildPackageToPackageProtectedMethod() {
    lintTask
      .files(
        TestFiles.packageProtectedAnnotation,
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test

              import br.com.orcinus.orca.std.visibility.PackageProtected

              class Api {
                @PackageProtected
                fun call() {
                }
              }
            """
          )
          .indented(),
        TestFiles.kotlin(
            """
              package br.com.orcinus.orca.std.visibility.lint.detection.test.consumer

              import br.com.orcinus.orca.std.visibility.lint.detection.test.Api

              private object Consumer {
                init {
                  Api().call()
                }
              }
            """
          )
          .indented()
      )
      .run()
      .expect("No warnings.")
  }
}
