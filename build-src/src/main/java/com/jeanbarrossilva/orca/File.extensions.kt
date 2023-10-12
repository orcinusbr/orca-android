package com.jeanbarrossilva.orca

import java.io.File
import java.util.Properties

/**
 * Creates [Properties] according to the `.properties` file named [name] within this directory.
 *
 * @param name Name of the file.
 */
fun File.properties(name: String): Properties {
  val file = File(this, "$name.properties")
  return Properties().apply { tryToLoad(file) }
}

/**
 * Loads the given [file] into these [Properties].
 *
 * @param file [File] to be loaded.
 */
private fun Properties.load(file: File) {
  file.inputStream().reader().use { load(it) }
}

/**
 * Loads the given [file] into these [Properties] if it's a normal file.
 *
 * @param file [File] to be loaded.
 */
private fun Properties.tryToLoad(file: File) {
  if (file.isFile) {
    load(file)
  }
}
