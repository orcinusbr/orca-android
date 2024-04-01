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

package br.com.orcinus.orca.std.injector.processor.inject

import br.com.orcinus.orca.ext.processing.addImports
import br.com.orcinus.orca.ext.processing.requireContainingFile
import br.com.orcinus.orca.std.injector.module.Inject
import br.com.orcinus.orca.std.injector.module.Module
import br.com.orcinus.orca.std.injector.module.injection.Injection
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
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * [SymbolProcessor] for ensuring the integrity of [Inject]-annotated properties and generating
 * extension functions that retrieve the declared dependencies.
 *
 * Reports an error if...
 * - [Injection]s aren't part of a specific [Module];
 * - [Injection]s are private;
 * - [Inject]-annotated properties don't return an [Injection]; or
 * - a [Module] that declares [Injection]s is private.
 */
class InjectProcessor private constructor(private val environment: SymbolProcessorEnvironment) :
  SymbolProcessor {
  /** [SymbolProcessorProvider] that provides an [InjectProcessor]. */
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
    generateExtensionFunctions(injectionDeclarations)
    return emptyList()
  }

  /**
   * Reports an error for each [KSPropertyDeclaration] within the [injectionDeclarations] that have
   * been created outside of a [Module]. For example:
   * ```
   * class MyModule(@Inject private val correctlyDeclaredDependency: Injection<Int>) : Module()
   *
   * @Inject
   * val incorrectlyDeclaredDependency = injectionOf { 0 }
   * ```
   *
   * In this case, the error would be reported on `incorrectlyDeclaredDependency`.
   *
   * @param injectionDeclarations Declared properties annotated with [Inject].
   */
  private fun reportErrorOnModuleUnrelatedInjections(
    injectionDeclarations: List<KSPropertyDeclaration>
  ) {
    injectionDeclarations
      .filterNot { it.isWithin<Module>() }
      .forEach { environment.logger.error("An injection should be part of a Module.", symbol = it) }
  }

  /**
   * Reports an error for each [KSPropertyDeclaration] within the [injectionDeclarations] that has a
   * return type other than [Injection].
   *
   * @param injectionDeclarations Declared properties annotated with [Inject].
   */
  private fun reportErrorOnMismatchingType(injectionDeclarations: List<KSPropertyDeclaration>) {
    injectionDeclarations.filterNot(KSPropertyDeclaration::isInjection).forEach {
      environment.logger.error(
        "An Inject-annotated property should return an Injection.",
        symbol = it
      )
    }
  }

  /**
   * Reports an error for each private [Module] that contains [Injection]s. They should be visible
   * to the extension properties that will be generated for their dependencies.
   *
   * @param injectionDeclarations Declared properties annotated with [Inject].
   */
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
   * Reports an error for each private [Injection]. This is necessary for the workaround of
   * suppressing their "unused" warning by referencing them when returning the dependency from their
   * respective extension property on the [Module] in which they've been declared.
   *
   * @param injectionDeclarations Declared properties with [Inject].
   */
  private fun reportErrorOnPrivateInjections(injectionDeclarations: List<KSPropertyDeclaration>) {
    injectionDeclarations.filter(KSPropertyDeclaration::isPrivate).forEach {
      environment.logger.error("An injection cannot be private.", symbol = it)
    }
  }

  /**
   * Generates extension functions for each of the [injectionDeclarations] for them to be easily
   * obtained instead of relying on the [Module.get]'s runtime type check.
   *
   * @param injectionDeclarations [KSPropertyDeclaration]s of the [Injection]s for which the
   *   extension properties will be generated.
   * @throws IllegalStateException If the [Module]s aren't part of a [KSFile] or the [KSType] of an
   *   injected dependency cannot be resolved.
   */
  @Throws(IllegalStateException::class)
  private fun generateExtensionFunctions(injectionDeclarations: List<KSPropertyDeclaration>) {
    injectionDeclarations
      .filter(KSPropertyDeclaration::isInjection)
      .groupBy { it.parentDeclaration as KSClassDeclaration }
      .forEach { (module, moduleInjections) ->
        createExtensionsFileSpec(module, moduleInjections)
          .writeTo(
            environment.codeGenerator,
            Dependencies(aggregating = true, module.requireContainingFile())
          )
      }
  }

  /**
   * Creates a [FileSpec] of a file for the given [moduleDeclaration] in which its
   * [injectionDeclarations]' extension properties are declared.
   *
   * @param moduleDeclaration [KSClassDeclaration] of the [Module] for which the [FileSpec] will be
   *   created.
   * @param injectionDeclarations [KSPropertyDeclaration]s of the [moduleDeclaration]'s
   *   [Injection]s.
   * @throws IllegalStateException If the [moduleDeclaration] isn't part of a [KSFile] or the
   *   [KSType] of an injected dependency cannot be resolved.
   */
  @Throws(IllegalStateException::class)
  private fun createExtensionsFileSpec(
    moduleDeclaration: KSClassDeclaration,
    injectionDeclarations: List<KSPropertyDeclaration>
  ): FileSpec {
    val packageName = moduleDeclaration.packageName.asString()
    val fileName = moduleDeclaration.simpleName.asString() + ".extensions"
    val moduleFile = moduleDeclaration.requireContainingFile()
    val extensionFunSpecs =
      injectionDeclarations.map { createExtensionFunSpec(moduleDeclaration, it) }
    return FileSpec.builder(packageName, fileName)
      .addImports(moduleFile)
      .apply { extensionFunSpecs.forEach(::addFunction) }
      .build()
  }

  /**
   * Creates a [FunSpec] of an extension property for the given [injectionDeclaration] that's
   * contained within the [moduleDeclaration].
   *
   * @param moduleDeclaration [KSClassDeclaration] of the [Module] in which the
   *   [injectionDeclaration] is.
   * @param injectionDeclaration [KSPropertyDeclaration] of the [Injection] for which the [FunSpec]
   *   will be created.
   * @throws IllegalStateException If the [KSType] of the injected dependency cannot be resolved.
   */
  @Throws(IllegalStateException::class)
  private fun createExtensionFunSpec(
    moduleDeclaration: KSClassDeclaration,
    injectionDeclaration: KSPropertyDeclaration
  ): FunSpec {
    val name = injectionDeclaration.simpleName.asString()
    val type =
      injectionDeclaration.type.resolve().arguments.last().type?.resolve()
        ?: throw IllegalStateException(
          "Cannot create extension property for a dependency with an unresolved KSType."
        )
    val moduleType = moduleDeclaration.asStarProjectedType()
    val moduleTypeName = moduleType.toTypeName()
    val typeDeclaration = type.declaration
    val moduleVisibility = moduleDeclaration.getVisibility()
    val typeVisibility = type.declaration.getVisibility()
    val visibility = minOf(moduleVisibility, typeVisibility).toKModifier() ?: KModifier.PUBLIC
    val typeDeclarationName = typeDeclaration.simpleName.asString()
    val moduleDeclarationName = moduleType.declaration.simpleName.asString()
    val parentAndModuleDeclarationName =
      (moduleDeclaration.parentDeclaration as? KSClassDeclaration)
        ?.simpleName
        ?.asString()
        ?.plus('.')
        .orEmpty()
        .plus(moduleDeclarationName)
    val typeName = type.toTypeName()
    return FunSpec.builder(name)
      .addKdoc(
        "[$typeDeclarationName] that's been injected into this " +
          "[$parentAndModuleDeclarationName]."
      )
      .addModifiers(visibility)
      .receiver(moduleTypeName)
      .addStatement(
        """
        return run {
            $name
            get()
        }
        """
          .trimIndent()
      )
      .returns(typeName)
      .build()
  }
}
