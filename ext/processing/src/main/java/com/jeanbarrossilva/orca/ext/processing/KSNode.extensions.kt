package com.jeanbarrossilva.orca.ext.processing

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSNode

/**
 * Requires the [KSFile] in which this [KSNode] is or throws an [IllegalStateException] if it
 * doesn't have one.
 *
 * @throws IllegalStateException If this [KSNode] isn't part of a [KSFile].
 */
fun KSNode.requireContainingFile(): KSFile {
  return containingFile ?: throw IllegalStateException("$this doesn't have a containing KSFile.")
}
