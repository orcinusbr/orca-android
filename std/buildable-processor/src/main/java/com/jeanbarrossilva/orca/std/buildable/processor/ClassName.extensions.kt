package com.jeanbarrossilva.orca.std.buildable.processor

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
