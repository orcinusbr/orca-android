package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.jeanbarrossilva.orca.std.injector.module.Inject

/** Gets the [Inject]-annotated injections. */
internal fun Resolver.getInjections(): Sequence<KSPropertyDeclaration> {
  return getSymbolsWithAnnotation(Inject::class.java.name).filterIsInstance<KSPropertyDeclaration>()
}
