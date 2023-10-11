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
        val injectionDeclarations = resolver.getInjections().toList()
        reportErrorOnModuleUnrelatedInjections(injectionDeclarations)
        reportErrorOnMismatchingType(injectionDeclarations)
        reportErrorOnPrivateModules(injectionDeclarations)
        reportErrorOnPrivateInjections(injectionDeclarations)
        generateExtensionProperties(injectionDeclarations)
        return emptyList()
    }

    /**
     * Reports an error for each [KSPropertyDeclaration] within the [injectionDeclarations] that
     * have been created outside of a [Module]. For example:
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
     * @param injectionDeclarations Declared properties annotated with [Inject].
     **/
    private fun reportErrorOnModuleUnrelatedInjections(injectionDeclarations: List<KSPropertyDeclaration>) {
        injectionDeclarations.filterNot { it.isWithin<Module>() }.forEach {
            environment.logger.error("An injection should be part of a Module.", symbol = it)
        }
    }

    /**
     * Reports an error for each [KSPropertyDeclaration] within the [injectionDeclarations] that
     * have a return type other than that of an injection, which is `Module.() -> Any`.
     *
     * @param injectionDeclarations Declared properties annotated with [Inject].
     **/
    private fun reportErrorOnMismatchingType(injectionDeclarations: List<KSPropertyDeclaration>) {
        injectionDeclarations
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
     * @param injectionDeclarations Declared properties annotated with [Inject].
     **/
    private fun reportErrorOnPrivateModules(injectionDeclarations: List<KSPropertyDeclaration>) {
        injectionDeclarations
            .filter { (it.parentDeclaration as KSClassDeclaration).isPrivate() }
            .forEach {
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
     * @param injectionDeclarations Declared properties with [Inject].
     **/
    private fun reportErrorOnPrivateInjections(injectionDeclarations: List<KSPropertyDeclaration>) {
        injectionDeclarations.filter(KSPropertyDeclaration::isPrivate).forEach {
            environment.logger.error("An injection cannot be private.", symbol = it)
        }
    }

    /**
     * Generates extension properties for each of the [injectionDeclarations] for them to be easily
     * obtained instead of relying on the [Module.get]'s runtime type check.
     *
     * @param injectionDeclarations Injections for which the extension properties will be generated.
     * @throws IllegalStateException If the [Module]s aren't part of a [KSFile] or the [KSType] of
     * an injected dependency cannot be resolved.
     **/
    @Throws(IllegalStateException::class)
    private fun generateExtensionProperties(injectionDeclarations: List<KSPropertyDeclaration>) {
        injectionDeclarations
            .filter(KSPropertyDeclaration::isInjection)
            .groupBy { it.parentDeclaration as KSClassDeclaration }
            .forEach { (module, moduleInjections) ->
                createExtensionsFileSpec(module, moduleInjections).writeTo(
                    environment.codeGenerator,
                    Dependencies(aggregating = true, module.requireContainingFile())
                )
            }
    }

    /**
     * Creates a [FileSpec] of a file for the given [moduleDeclaration] in which its
     * [injectionDeclarations]' extension properties are declared.
     *
     * @param moduleDeclaration [KSClassDeclaration] of the [Module] for which the [FileSpec] will
     * be created.
     * @param injectionDeclarations [KSPropertyDeclaration]s of the [moduleDeclaration]'s
     * injections.
     * @throws IllegalStateException If the [moduleDeclaration] isn't part of a [KSFile] or the
     * [KSType] of an injected dependency cannot be resolved.
     **/
    @Throws(IllegalStateException::class)
    private fun createExtensionsFileSpec(
        moduleDeclaration: KSClassDeclaration,
        injectionDeclarations: List<KSPropertyDeclaration>
    ): FileSpec {
        val packageName = moduleDeclaration.packageName.asString()
        val fileName = moduleDeclaration.simpleName.asString() + ".extensions"
        val moduleFile = moduleDeclaration.requireContainingFile()
        val extensionPropertySpecs =
            injectionDeclarations.map { createExtensionPropertySpec(moduleDeclaration, it) }
        return FileSpec
            .builder(packageName, fileName)
            .addImports(moduleFile)
            .apply { extensionPropertySpecs.forEach(::addProperty) }
            .build()
    }

    /**
     * Creates a [PropertySpec] of an extension property for the given [injectionDeclaration] that's
     * contained within the [moduleDeclaration].
     *
     * @param moduleDeclaration [KSClassDeclaration] of the [Module] in which the
     * [injectionDeclaration] is.
     * @param injectionDeclaration [KSPropertyDeclaration] of the injection for which the
     * [PropertySpec] will be created.
     * @throws IllegalStateException If the [KSType] of the injected dependency cannot be resolved.
     **/
    @Throws(IllegalStateException::class)
    private fun createExtensionPropertySpec(
        moduleDeclaration: KSClassDeclaration,
        injectionDeclaration: KSPropertyDeclaration
    ): PropertySpec {
        val name = injectionDeclaration.simpleName.asString()
        val type = injectionDeclaration
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
        val moduleType = moduleDeclaration.asStarProjectedType()
        val moduleTypeName = moduleType.toTypeName()
        val typeDeclaration = type.declaration
        val moduleVisibility = moduleDeclaration.getVisibility()
        val typeVisibility = type.declaration.getVisibility()
        val visibility = minOf(moduleVisibility, typeVisibility).toKModifier() ?: KModifier.PUBLIC
        val typeDeclarationName = typeDeclaration.simpleName.asString()
        val moduleDeclarationName = moduleType.declaration.simpleName.asString()
        val getterFunSpec = createExtensionPropertyGetterFunSpec(name, typeDeclarationName)
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
     * @param injectionName Name of the injection from which the extension derives, used for the
     * sole purpose of referencing and suppressing the original property's "unused" warning.
     * @param returnTypeSimpleName Simple name of the getter's return type declaration.
     **/
    private fun createExtensionPropertyGetterFunSpec(
        injectionName: String,
        returnTypeSimpleName: String
    ): FunSpec {
        return FunSpec
            .getterBuilder()
            .addStatement(
                """
                    return run {
                        $injectionName
                        get<$returnTypeSimpleName>()
                    }
                """
                    .trimIndent()
            )
            .build()
    }
}
