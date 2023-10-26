package com.jeanbarrossilva.orca.ext.processing

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec

/**
 * Adds the imports of the given [file] to the [FileSpec] being built.
 *
 * @param file [KSFile] whose imported structures will be added.
 */
fun FileSpec.Builder.addImports(file: KSFile): FileSpec.Builder {
  return apply { file.imports.forEach { addImport(it.asString()) } }
}
