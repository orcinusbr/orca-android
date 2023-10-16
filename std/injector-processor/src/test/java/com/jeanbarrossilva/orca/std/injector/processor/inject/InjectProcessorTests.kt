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
