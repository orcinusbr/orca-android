package com.jeanbarrossilva.orca.std.providable.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.jeanbarrossilva.orca.std.providable.Providable

/** Gets the [Providable]-annotated [KSClassDeclaration]s. */
internal fun Resolver.getProvidableDeclarations(): Sequence<KSClassDeclaration> {
  return getSymbolsWithAnnotation(Providable::class.java.name)
    .filterIsInstance<KSClassDeclaration>()
}
