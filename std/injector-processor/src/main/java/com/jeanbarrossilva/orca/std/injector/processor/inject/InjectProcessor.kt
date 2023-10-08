package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.jeanbarrossilva.orca.std.injector.module.Module

/**
 * [SymbolProcessor] for ensuring the integrity of [Inject]-annotated properties, reporting an error
 * if they're not part of a specific [Module].
 **/
class InjectProcessor private constructor(private val environment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    /** [SymbolProcessorProvider] that provides an [InjectProcessor]. **/
    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): InjectProcessor {
            return InjectProcessor(environment)
        }
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val injections = resolver.getInjections().toList()
        errorOnModuleUnrelatedInjections(injections)
        return emptyList()
    }

    /**
     * Reports an error for each [KSPropertyDeclaration] within the [injections] that have been
     * created outside of a [Module]. For example:
     *
     * ```
     * class MyModule(@Inject private val correctlyDeclaredDependency: Module.() -> Int) : Module()
     *
     * @Inject
     * val incorrectlyDeclaredDependency: Module.() -> Int = { 0 }
     * ```
     *
     * In this case, the error would be reported on `incorrectlyDeclaredDependency`.
     **/
    private fun errorOnModuleUnrelatedInjections(injections: List<KSPropertyDeclaration>) {
        injections.filterNot { it.isWithin<Module>() }.forEach {
            environment.logger.error("An injection should be part of a Module.", symbol = it)
        }
    }
}
