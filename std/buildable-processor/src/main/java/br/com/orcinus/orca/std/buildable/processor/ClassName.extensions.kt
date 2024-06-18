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

package br.com.orcinus.orca.std.buildable.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty

/**
 * Parameterizes this [ClassName] if [typeNames] isn't empty.
 *
 * @param typeNames [TypeName]s to parameterize this [ClassName] with.
 */
internal fun ClassName.parameterizedOrNotBy(typeNames: List<TypeName>): TypeName {
  return typeNames.ifNotEmpty { parameterizedBy(typeNames) } ?: this
}
