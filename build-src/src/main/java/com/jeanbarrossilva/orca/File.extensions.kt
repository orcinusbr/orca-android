/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
