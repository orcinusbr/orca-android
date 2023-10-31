package com.jeanbarrossilva.orca.std.buildable.processor

import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/** Whether this [KSDeclaration] is abstract. */
internal val KSDeclaration.isAbstract
  get() =
    when (this) {
      is KSFunctionDeclaration -> isAbstract
      is KSPropertyDeclaration -> isAbstract()
      else -> false
    }
