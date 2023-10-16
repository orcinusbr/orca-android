package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference

/**
 * All super types of each subclass that the class to which this [KSClassDeclaration] refers
 * subclasses alongside its own.
 */
internal val KSClassDeclaration.flattenedSuperTypes: Sequence<KSTypeReference>
  get() =
    superTypes +
      superTypes.flatMap { (it.resolve().declaration as KSClassDeclaration).flattenedSuperTypes }
