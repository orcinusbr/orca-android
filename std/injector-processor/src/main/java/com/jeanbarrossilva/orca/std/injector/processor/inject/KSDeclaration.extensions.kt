/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.typeNameOf

/**
 * Whether this [KSDeclaration] is within [T]'s [KSClassDeclaration].
 *
 * @param T Structure whose ownership of this [KSDeclaration] will be verified.
 */
internal inline fun <reified T : Any> KSDeclaration.isWithin(): Boolean {
  val parentDeclaration = parentDeclaration as? KSClassDeclaration ?: return false
  val typeName = typeNameOf<T>()
  return parentDeclaration.flattenedSuperTypes
    .map(KSTypeReference::toTypeName)
    .any(typeName::equals)
}
