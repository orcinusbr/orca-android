package com.jeanbarrossilva.orca.std.providable.processor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import kotlin.test.Test
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

internal class ProvidableProcessorTests {
  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesProvider() {
    val source =
      SourceFile.kotlin(
        "MyInterface.kt",
        """
          import com.jeanbarrossilva.orca.std.providable.Providable

          /** My interface. 😎 */
          @Providable
          interface MyInterface {
            /** My [Int]. 🫰🏽 */
            val myInt: Int
          }
        """
          .trimIndent()
      )
    val compilation =
      KotlinCompilation().apply {
        inheritClassPath = true
        kspWithCompilation = true
        sources = listOf(source)
        symbolProcessorProviders = listOf(ProvidableProcessor.Provider())
      }
    val result = compilation.compile()
    val providerClass = result.classLoader.loadClass("MyInterfaceProvider")
    val providerMethod = providerClass.declaredMethods.single()
    assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
    assertThat(providerMethod.name).isEqualTo(ProvidableProcessor.PROVIDER_METHOD_NAME)
    assertThat(providerMethod.parameterTypes).isEqualTo(arrayOf(Int::class.java))
    assertThat(providerMethod.returnType.name).isEqualTo("MyInterface")
  }
}
