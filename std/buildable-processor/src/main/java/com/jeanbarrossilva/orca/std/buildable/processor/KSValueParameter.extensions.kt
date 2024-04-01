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

package com.jeanbarrossilva.orca.std.buildable.processor

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSValueParameter
import com.jeanbarrossilva.orca.ext.processing.compile
import com.jeanbarrossilva.orca.ext.processing.requireContainingFile
import org.jetbrains.kotlin.psi.KtClassLikeDeclaration
import org.jetbrains.kotlin.psi.KtParameterList
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType

/**
 * [String] version of the expression whose returning value has been attributed as the default one
 * for this [KSValueParameter].
 */
internal val KSValueParameter.defaultValue
  get() =
    requireContainingFile()
      .compile()
      ?.flattenChildren()
      ?.filterIsInstance<KtClassLikeDeclaration>()
      ?.find {
        it.fqName?.asString() ==
          (parent as KSFunctionDeclaration).parentDeclaration?.qualifiedName?.asString()
      }
      ?.getChildrenOfType<KtPrimaryConstructor>()
      ?.single()
      ?.getChildrenOfType<KtParameterList>()
      ?.single()
      ?.parameters
      ?.find { it.name == name?.asString() }
      ?.defaultValue
      ?.text

/**
 * Requires the name of this [KSValueParameter] or throws an [IllegalStateException] if it doesn't
 * have one.
 *
 * @throws IllegalStateException If it doesn't have a name.
 */
@Throws(IllegalStateException::class)
internal fun KSValueParameter.requireName(): KSName {
  return name ?: throw IllegalStateException("Nameless value parameter: $this.")
}
