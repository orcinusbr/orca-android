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
