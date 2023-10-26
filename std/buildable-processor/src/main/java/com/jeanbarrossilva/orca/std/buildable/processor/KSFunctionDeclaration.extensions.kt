package com.jeanbarrossilva.orca.std.buildable.processor

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.jeanbarrossilva.orca.ext.processing.compile
import com.jeanbarrossilva.orca.ext.processing.requireContainingFile
import org.jetbrains.kotlin.psi.KtNamedFunction

/** Body of this [KSFunctionDeclaration]. */
internal val KSFunctionDeclaration.body: String?
  get() {
    return requireContainingFile()
      .compile()
      ?.flattenChildren()
      ?.filterIsInstance<KtNamedFunction>()
      ?.single { it.fqName?.asString() == qualifiedName?.asString() }
      ?.bodyExpression
      ?.text
      ?.replaceLast("return ", "")
  }
