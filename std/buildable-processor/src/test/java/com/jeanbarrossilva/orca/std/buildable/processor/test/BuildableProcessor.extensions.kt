package com.jeanbarrossilva.orca.std.buildable.processor.test

import com.jeanbarrossilva.orca.std.buildable.processor.BuildableProcessor
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

/**
 * Processes the annotations by compiling the given [file].
 *
 * @param file [SourceFile] to be compiled.
 */
@OptIn(ExperimentalCompilerApi::class)
internal fun BuildableProcessor.Companion.process(file: SourceFile): JvmCompilationResult {
  return KotlinCompilation()
    .apply {
      inheritClassPath = true
      kspIncremental = true
      kspWithCompilation = true
      sources = listOf(file)
      symbolProcessorProviders = listOf(BuildableProcessor.Provider())
    }
    .compile()
}
