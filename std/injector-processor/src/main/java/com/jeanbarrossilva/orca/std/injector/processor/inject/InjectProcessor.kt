package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.isPrivate
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * [SymbolProcessor] for ensuring the integrity of [Inject]-annotated properties and generating
 * extension properties that retrieve the declared dependencies. Also reports an error if...
 *
 * - injections aren't part of a specific [Module];
 * - injections are private;
 * - injections have a return type different from `Module.() -> Any`; or
 * - a [Module] that declares injections is private.
 **/
class InjectProcessor private constructor(private val environment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    /** [SymbolProcessorProvider] that provides an [InjectProcessor]. **/
    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): InjectProcessor {
            return InjectProcessor(environment)
        }
    }

    @Throws(IllegalStateException::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val injections = resolver.getInjections().toList()
        reportErrorOnModuleUnrelatedInjections(injections)
        reportErrorOnMismatchingType(injections)
        reportErrorOnPrivateModules(injections)
        reportErrorOnPrivateInjections(injections)
        generateExtensionProperties(injections)
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
     *
     * @param injections Declared properties annotated with [Inject].
     **/
    private fun reportErrorOnModuleUnrelatedInjections(injections: List<KSPropertyDeclaration>) {
        injections.filterNot { it.isWithin<Module>() }.forEach {
            environment.logger.error("An injection should be part of a Module.", symbol = it)
        }
    }

    /**
     * Reports an error for each [KSPropertyDeclaration] within the [injections] that have a return
     * type other than that of an injection, which is `Module.() -> Any`.
     *
     * @param injections Declared properties annotated with [Inject].
     **/
    private fun reportErrorOnMismatchingType(injections: List<KSPropertyDeclaration>) {
        injections
            .filterNot(KSPropertyDeclaration::isInjection)
            .forEach {
                environment.logger.error(
                    "An injection should return have a return type of `Module.() -> Any`.",
                    symbol = it
                )
            }
    }

    /**
     * Reports an error for each private [Module] that contains injections. They should be visible
     * to the extension properties that will be generated for their dependencies.
     *
     * @param injections Declared properties annotated with [Inject].
     **/
    private fun reportErrorOnPrivateModules(injections: List<KSPropertyDeclaration>) {
        injections.filter { (it.parentDeclaration as KSClassDeclaration).isPrivate() }.forEach {
            environment.logger.error(
                "A Module with declared injections cannot be private.",
                symbol = it
            )
        }
    }

    /**
     * Reports an error for each private injection. This is necessary for the workaround of
     * suppressing their "unused" warning by referencing them when returning the dependency from
     * their respective extension property on the [Module] in which they've been declared.
     *
     * @param injections Declared properties with [Inject].
     **/
    private fun reportErrorOnPrivateInjections(injections: List<KSPropertyDeclaration>) {
        injections.filter(KSPropertyDeclaration::isPrivate).forEach {
            environment.logger.error("An injection cannot be private.", symbol = it)
        }
    }

    /**
     * Generates extension properties for each of the [injections] for them to be easily obtained
     * instead of relying on the [Module.get]'s runtime type check.
     *
     * @param injections Injections for which the extension properties will be generated.
     * @throws IllegalStateException If the [Module]s aren't part of a [KSFile] or the [KSType] of
     * an injected dependency cannot be resolved.
     **/
    @Throws(IllegalStateException::class)
    private fun generateExtensionProperties(injections: List<KSPropertyDeclaration>) {
        injections
            .filter(KSPropertyDeclaration::isInjection)
            .groupBy { it.parentDeclaration as KSClassDeclaration }
            .forEach { (module, moduleInjections) ->
                createExtensionsFileSpec(module, moduleInjections).writeTo(
                    environment.codeGenerator,
                    Dependencies(
                        aggregating = true,
                        module.requireContainingFile(),
                        *moduleInjections
                            .map(KSPropertyDeclaration::requireContainingFile)
                            .toTypedArray()
                    )
                )
            }
    }

    /**
     * Creates a [FileSpec] of a file for the given [module] in which its [injections]' extension
     * properties are declared.
     *
     * @param module [KSClassDeclaration] of the [Module] for which the [FileSpec] will be created.
     * @param injections [KSPropertyDeclaration]s of the [module]'s injections.
     * @throws IllegalStateException If the [module] isn't part of a [KSFile] or the [KSType] of an
     * injected dependency cannot be resolved.
     **/
    @Throws(IllegalStateException::class)
    private fun createExtensionsFileSpec(
        module: KSClassDeclaration,
        injections: List<KSPropertyDeclaration>
    ): FileSpec {
        val packageName = module.packageName.asString()
        val fileName = module.simpleName.asString() + ".extensions"
        val moduleFile = module.requireContainingFile()
        val extensionPropertySpecs = injections.map { createExtensionPropertySpec(module, it) }
        return FileSpec
            .builder(packageName, fileName)
            .addImports(moduleFile)
            .apply { extensionPropertySpecs.forEach(::addProperty) }
            .build()
    }

    /**
     * Creates a [PropertySpec] of an extension property for the given [injection] that's contained
     * within the [module].
     *
     * @param module [KSClassDeclaration] of the [Module] in which the [injection] is.
     * @param injection [KSPropertyDeclaration] of the injection for which the [PropertySpec] will
     * be created.
     * @throws IllegalStateException If the [KSType] of the injected dependency cannot be resolved.
     **/
    @Throws(IllegalStateException::class)
    private fun createExtensionPropertySpec(
        module: KSClassDeclaration,
        injection: KSPropertyDeclaration
    ): PropertySpec {
        val name = injection.simpleName.asString()
        val type = injection
            .type
            .resolve()
            .arguments
            .last()
            .type
            ?.resolve()
            ?: throw IllegalStateException(
                "Cannot create extension property for a dependency with an unresolved KSType."
            )
        val typeName = type.toTypeName()
        val moduleType = module.asStarProjectedType()
        val moduleTypeName = moduleType.toTypeName()
        val typeDeclaration = type.declaration
        val moduleVisibility = module.getVisibility()
        val typeVisibility = type.declaration.getVisibility()
        val visibility = minOf(moduleVisibility, typeVisibility).toKModifier() ?: KModifier.PUBLIC
        val typeDeclarationName = typeDeclaration.simpleName.asString()
        val moduleDeclarationName = moduleType.declaration.simpleName.asString()
        val getterFunSpec = createExtensionPropertyGetterFunSpec(typeDeclarationName)
        return PropertySpec
            .builder(name, typeName)
            .addKdoc(
                "[$typeDeclarationName] that's been injected into this [$moduleDeclarationName]."
            )
            .addModifiers(visibility)
            .receiver(moduleTypeName)
            .getter(getterFunSpec)
            .build()
    }

    /**
     * Creates a [FunSpec] of a dependency's extension property getter.
     *
     * @param returnTypeSimpleName Simple name of the getter's return type declaration.
     **/
    private fun createExtensionPropertyGetterFunSpec(returnTypeSimpleName: String): FunSpec {
        return FunSpec.getterBuilder().addStatement("return get<$returnTypeSimpleName>()").build()
    }
}
