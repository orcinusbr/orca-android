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

package br.com.orcinus.orca.std.buildable.processor.test

import br.com.orcinus.orca.std.buildable.processor.BuildableProcessor
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

/**
 * Processes the annotations by compiling the given [files].
 *
 * @param files [SourceFile]s to be compiled.
 */
@OptIn(ExperimentalCompilerApi::class)
internal fun BuildableProcessor.Companion.process(vararg files: SourceFile): JvmCompilationResult {
  return KotlinCompilation()
    .apply {
      inheritClassPath = true
      kspIncremental = true
      kspWithCompilation = true
      sources = files.toList()
      symbolProcessorProviders = listOf(BuildableProcessor.Provider())
    }
    .compile()
}
