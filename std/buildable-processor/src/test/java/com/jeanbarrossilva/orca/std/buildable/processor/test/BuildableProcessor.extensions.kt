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
