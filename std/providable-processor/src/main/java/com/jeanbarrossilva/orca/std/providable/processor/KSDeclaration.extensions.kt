package com.jeanbarrossilva.orca.std.providable.processor

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile

/**
 * Requires the [KSFile] in which this [KSDeclaration] is or throws an [IllegalStateException] if it
 * doesn't have one.
 *
 * @throws IllegalStateException If this [KSDeclaration] isn't part of a [KSFile].
 */
internal fun KSDeclaration.requireContainingFile(): KSFile {
  return containingFile ?: throw IllegalStateException("$this doesn't have a containing KSFile.")
}
