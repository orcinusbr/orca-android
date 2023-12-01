/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.buildable.processor.test

import com.tschuchort.compiletesting.JvmCompilationResult
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

/** Last message that's been printed by the compilation process. */
@OptIn(ExperimentalCompilerApi::class)
internal val JvmCompilationResult.lastMessage
  get() = messages.split('\n').last(String::isNotEmpty)
