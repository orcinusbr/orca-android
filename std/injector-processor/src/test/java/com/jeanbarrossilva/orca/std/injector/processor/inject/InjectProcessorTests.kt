/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.std.injector.processor.inject

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import kotlin.test.Test
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

internal class InjectProcessorTests {
  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun doesNotReportErrorOnInjectionWithinModuleThatIndirectlySubclassesTheBaseClass() {
    val source =
      SourceFile.kotlin(
        "Modules.kt",
        """
            import com.jeanbarrossilva.orca.std.injector.module.Inject
            import com.jeanbarrossilva.orca.std.injector.module.Module

            class SubModule(@Inject override val dependency: Module.() -> Int = { 0 }) :
              SuperModule(dependency)

            abstract class SuperModule(@Inject open val dependency: Module.() -> Int = { 0 }) :
              Module()
        """
          .trimIndent()
      )
    val result =
      KotlinCompilation()
        .apply {
          inheritClassPath = true
          sources = listOf(source)
          symbolProcessorProviders = listOf(InjectProcessor.Provider())
        }
        .compile()
    assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
  }
}
