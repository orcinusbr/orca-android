package com.jeanbarrossilva.orca.std.providable.processor

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.KSValueParameter
import com.jeanbarrossilva.orca.std.providable.Providable
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.toTypeParameterResolver
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * [SymbolProcessor] that generates providers for [Providable]-annotated interfaces.
 *
 * @param environment [SymbolProcessorEnvironment] in which the errors will be reported and/or the
 *   code will be generated.
 */
class ProvidableProcessor private constructor(private val environment: SymbolProcessorEnvironment) :
  SymbolProcessor {
  /** [SymbolProcessorProvider] that provides a [ProvidableProcessor]. */
  class Provider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): ProvidableProcessor {
      return ProvidableProcessor(environment)
    }
  }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val providableDeclarations = resolver.getProvidableDeclarations()
    reportErrorOnNonInterfaces(providableDeclarations)
    generateProviders(providableDeclarations)
    return emptyList()
  }

  /**
   * Reports an error for each [Providable] that's not an interface.
   *
   * @param providableDeclarations [KSClassDeclaration] of the interfaces annotated with
   *   [Providable].
   */
  private fun reportErrorOnNonInterfaces(providableDeclarations: Sequence<KSClassDeclaration>) {
    providableDeclarations
      .filterNot { it.classKind == ClassKind.INTERFACE }
      .forEach { environment.logger.error("Only interfaces can be providable.", symbol = it) }
  }

  /**
   * Generates the providers for each of the [providableDeclarations].
   *
   * @param providableDeclarations [KSClassDeclaration] of the interfaces annotated with
   *   [Providable].
   */
  private fun generateProviders(providableDeclarations: Sequence<KSClassDeclaration>) {
    providableDeclarations.forEach {
      createProviderFileSpec(it)
        .writeTo(
          environment.codeGenerator,
          Dependencies(aggregating = true, it.requireContainingFile())
        )
    }
  }

  /**
   * Creates a [FileSpec] in which the provider interface for the given [providableDeclaration] is
   * declared.
   *
   * @param providableDeclaration [KSClassDeclaration] of the [Providable] for which the [FileSpec]
   *   is.
   */
  private fun createProviderFileSpec(providableDeclaration: KSClassDeclaration): FileSpec {
    val packageName = providableDeclaration.packageName.asString()
    val name = providableDeclaration.simpleName.asString()
    val provideMethodParameters = providableDeclaration.getDeclaredProperties()
    val imports =
      provideMethodParameters
        .map(KSPropertyDeclaration::type)
        .mapNotNull { it.resolve().declaration.qualifiedName?.asString() }
        .filterNot { providableDeclaration.qualifiedName?.asString()?.let(it::startsWith) ?: true }
    val interfaceSpec = createProviderInterfaceSpec(providableDeclaration, provideMethodParameters)
    return FileSpec.builder(packageName, name)
      .apply { imports.forEach { addImport(it, "") } }
      .addType(interfaceSpec)
      .build()
  }

  /**
   * Creates an interface [TypeSpec] with a `provide` method for providing an instance of the type
   * of the [providableDeclaration].
   *
   * @param providableDeclaration [KSClassDeclaration] of the [Providable] for which the [TypeSpec]
   *   is.
   * @param provideMethodParameters [KSPropertyDeclaration]s to be specified as the parameters of
   *   the `provide` method to be declared.
   */
  private fun createProviderInterfaceSpec(
    providableDeclaration: KSClassDeclaration,
    provideMethodParameters: Sequence<KSPropertyDeclaration>
  ): TypeSpec {
    val type = providableDeclaration.asStarProjectedType()
    val typeName = type.toTypeName()
    val typeDeclarationName = type.declaration.simpleName.asString()
    val typeDeclarationNameIndefiniteArticle = IndefiniteArticle.of(typeDeclarationName)
    val typeDeclarationNameKDocReferencePrecededByIndefiniteArticle =
      "$typeDeclarationNameIndefiniteArticle [$typeDeclarationName]"
    val typeVariableName = TypeVariableName(PROVIDER_CLASS_TYPE_VARIABLE_NAME)
    val provideFunSpec =
      createProvideFunSpec(
        typeName,
        typeDeclarationNameKDocReferencePrecededByIndefiniteArticle,
        providableDeclaration.typeParameters,
        provideMethodParameters
      )
    return TypeSpec.interfaceBuilder(name = typeDeclarationName + "Provider")
      .addKdoc(
        """
          Provides $typeDeclarationNameKDocReferencePrecededByIndefiniteArticle through
          [$PROVIDER_METHOD_NAME].

          @param $PROVIDER_CLASS_TYPE_VARIABLE_NAME Instance to be provided. 
        """
          .trimIndent()
      )
      .addModifiers(KModifier.FUN)
      .addTypeVariable(typeVariableName)
      .addFunction(provideFunSpec)
      .build()
  }

  /**
   * Creates a [FunSpec] of the `provide` method responsible for providing an instance of a class
   * annotated with [Providable].
   *
   * @param typeName [TypeName] of the annotated interface.
   * @param typeDeclarationNameKDocReferencePrecededByIndefiniteArticle KDoc reference to the
   *   [KSDeclaration] of the interface's type containing the appropriate indefinite article at the
   *   beginning.
   * @param typeParameters [KSTypeParameter]s of the annotated interface.
   * @param parameters [KSPropertyDeclaration] to be specified by the method.
   */
  private fun createProvideFunSpec(
    typeName: TypeName,
    typeDeclarationNameKDocReferencePrecededByIndefiniteArticle: String,
    typeParameters: List<KSTypeParameter>,
    parameters: Sequence<KSPropertyDeclaration>
  ): FunSpec {
    val parameterSpecs = parameters.map {
      createProvideFunParameterSpec(it.docString, typeParameters, it)
    }
    return FunSpec.builder(PROVIDER_METHOD_NAME)
      .addKdoc("Provides $typeDeclarationNameKDocReferencePrecededByIndefiniteArticle.")
      .addModifiers(KModifier.ABSTRACT)
      .addParameters(parameterSpecs.asIterable())
      .returns(typeName)
      .build()
  }

  /**
   * Creates a parameter of the `provide` method of a provider.
   *
   * @param documentation Documentation to be associated to the parameter.
   * @param typeParameters [KSTypeParameter]s of the [Providable].
   * @param parameter [KSPropertyDeclaration] for which the [ParameterSpec] will be created.
   */
  private fun createProvideFunParameterSpec(
    documentation: String?,
    typeParameters: List<KSTypeParameter>,
    parameter: KSPropertyDeclaration
  ): ParameterSpec {
    val name = parameter.simpleName.asString()
    val type = parameter.type
    val typeParameterResolver = typeParameters.toTypeParameterResolver()
    val typeName = type.toTypeName(typeParameterResolver)
    return ParameterSpec.builder(name, typeName).apply { documentation?.let(::addKdoc) }.build()
  }

  companion object {
    /** Name of the provider interface method. */
    private const val PROVIDER_METHOD_NAME = "provide"

    /** Name of the variable that represents the [Providable]-annotated type within its provider. */
    private const val PROVIDER_CLASS_TYPE_VARIABLE_NAME = "T"
  }
}
