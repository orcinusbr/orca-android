/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.std.injector.processor.inject

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.std.injector.processor.BuildConfig
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
            import br.com.orcinus.orca.std.injector.module.Inject
            import br.com.orcinus.orca.std.injector.module.Module
            import br.com.orcinus.orca.std.injector.module.injection.Injection
            import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf

            class SubModule(
              @Inject override val dependency: Injection<Int> = lazyInjectionOf { 0 }
            ) : SuperModule(dependency)

            abstract class SuperModule(
              @Inject open val dependency: Injection<Int> = lazyInjectionOf { 0 }
            ) : Module()
        """
          .trimIndent()
      )
    val result =
      KotlinCompilation()
        .apply {
          inheritClassPath = true
          jvmTarget = BuildConfig.javaVersion
          sources = listOf(source)
          symbolProcessorProviders += InjectProcessor.Provider()
        }
        .compile()
    assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
  }
}
