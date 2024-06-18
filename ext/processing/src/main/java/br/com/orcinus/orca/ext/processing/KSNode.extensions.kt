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

package br.com.orcinus.orca.ext.processing

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
