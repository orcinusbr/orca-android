package com.jeanbarrossilva.orca.std.buildable.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.jeanbarrossilva.orca.std.buildable.Buildable

/** Gets the [KSClassDeclaration]s that have been annotated with [Buildable]. */
internal fun Resolver.getBuildableDeclarations(): Sequence<KSClassDeclaration> {
  return getSymbolsWithAnnotation(Buildable::class.java.name).filterIsInstance<KSClassDeclaration>()
}
