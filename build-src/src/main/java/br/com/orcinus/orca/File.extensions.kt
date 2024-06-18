/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca

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
