package com.jeanbarrossilva.orca.std.buildable.processor

import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Visibility

/** Whether this [KSDeclaration] is abstract. */
internal val KSDeclaration.isAbstract
  get() =
    when (this) {
      is KSFunctionDeclaration -> isAbstract
      is KSPropertyDeclaration -> isAbstract()
      else -> false
    }

/** [Visibility] of the root [KSDeclaration]. */
internal val KSDeclaration.rootDeclarationVisibility: Visibility
  get() {
    var rootDeclaration = this
    while (rootDeclaration.parentDeclaration != null) {
      rootDeclaration = rootDeclaration.parentDeclaration!!
    }
    return rootDeclaration.getVisibility()
  }
