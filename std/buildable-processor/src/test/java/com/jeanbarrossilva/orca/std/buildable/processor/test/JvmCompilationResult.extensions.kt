package com.jeanbarrossilva.orca.std.buildable.processor.test

import com.tschuchort.compiletesting.JvmCompilationResult
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

/** Last message that's been printed by the compilation process. */
@OptIn(ExperimentalCompilerApi::class)
internal val JvmCompilationResult.lastMessage
  get() = messages.split('\n').last(String::isNotEmpty)
