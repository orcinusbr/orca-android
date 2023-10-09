package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.jeanbarrossilva.orca.std.injector.module.Module
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.typeNameOf

/** Whether this [KSPropertyDeclaration]'s return type is considered to be an injection. **/
internal val KSPropertyDeclaration.isInjection
    get() = with(type.resolve().arguments.map(KSTypeArgument::toTypeName)) {
        firstOrNull() == typeNameOf<Module>() &&
            !last().isNullable && last().toString() != "kotlin.Nothing"
    }
