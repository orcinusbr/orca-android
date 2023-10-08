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
 **/
internal inline fun <reified T : Any> KSDeclaration.isWithin(): Boolean {
    val parentDeclaration = parentDeclaration as? KSClassDeclaration ?: return false
    val typeName = typeNameOf<T>()
    return parentDeclaration.superTypes.map(KSTypeReference::toTypeName).any(typeName::equals)
}
