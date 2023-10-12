package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec

/**
 * Adds the imports of the given [file] to the [FileSpec] being built.
 *
 * @param file [KSFile] whose imported structures will be added.
 */
internal fun FileSpec.Builder.addImports(file: KSFile): FileSpec.Builder {
  return apply { file.imports.forEach { addImport(it.asString()) } }
}
