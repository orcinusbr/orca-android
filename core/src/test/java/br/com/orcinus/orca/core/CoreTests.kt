/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core

import com.lemonappdev.konsist.api.Konsist.scopeFromProject
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.ext.list.constructors
import com.lemonappdev.konsist.api.provider.KoKDocProvider
import com.lemonappdev.konsist.api.provider.modifier.KoCompanionModifierProvider
import com.lemonappdev.konsist.api.provider.modifier.KoOverrideModifierProvider
import com.lemonappdev.konsist.api.verify.assertTrue
import kotlin.test.Test

internal class CoreTests {
  private val scope = scopeFromProject(MODULE_NAME, sourceSetName = "main")

  @Test
  fun areNonCompanionObjectAndNonOverrideDeclarationsDocumented() {
    scope
      .declarations(includeLocal = false)
      .filterIsInstance<KoKDocProvider>()
      .filterNot { it is KoCompanionModifierProvider || it is KoOverrideModifierProvider }
      .assertTrue(
        additionalMessage =
          "Each non-companion-object and -override core declaration should be documented.",
        function = KoKDocProvider::hasKDoc
      )
  }

  @Test
  fun constructorsOfAbstractClassesAreMarkedAsInternalCoreApi() {
    scope.classes().filter(KoClassDeclaration::hasPublicOrDefaultModifier).constructors.assertTrue(
      additionalMessage = "Public core classes should only be instantiated by core variants."
    ) {
      it.hasAnnotationOf(InternalCoreApi::class) || it.hasPrivateModifier || it.hasInternalModifier
    }
  }

  @Test
  fun areFunctionalClassesTested() {
    val functionalClasses =
      scope.classes().filter { `class` ->
        `class`.hasFunction { function ->
          !function.hasNameMatching(Regex("equals|hashCode|toString"))
        }
      }
    if (functionalClasses.isNotEmpty()) {
      val testScope = scopeFromProject(MODULE_NAME, sourceSetName = "test")
      val untestedFunctionalClassesIndices = ArrayList<Int>(functionalClasses.size)
      for ((functionalClassIndex, functionalClass) in functionalClasses.withIndex()) {
        val functionalClassPackageName = functionalClass.packagee?.name ?: continue
        testScope
          .classes()
          .filter { it.resideInPackage(functionalClassPackageName) }
          .ifEmpty { untestedFunctionalClassesIndices += functionalClassIndex }
      }
      if (untestedFunctionalClassesIndices.isNotEmpty()) {
        error(
          "Each functional class should be thoroughly tested. No tests were found for:\n" +
            "${untestedFunctionalClassesIndices.joinToString(separator = ";\n", prefix = "- ") { functionalClasses[it].name }}."
        )
      }
    }
  }

  companion object {
    private const val MODULE_NAME = "core"
  }
}
