package com.jeanbarrossilva.orca.std.buildable.processor

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.isPrivate
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.jeanbarrossilva.orca.ext.processing.addImports
import com.jeanbarrossilva.orca.ext.processing.requireContainingFile
import com.jeanbarrossilva.orca.std.buildable.Buildable
import com.jeanbarrossilva.orca.std.buildable.processor.grammar.IndefiniteArticle
import com.jeanbarrossilva.orca.std.buildable.processor.grammar.PossessiveApostrophe
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.toTypeParameterResolver
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.typeNameOf
import org.jetbrains.kotlin.utils.addToStdlib.applyIf
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty

/**
 * [SymbolProcessor] that ensures the integrity of [Buildable]-annotated classes, generating their
 * builders or reporting an error if they're either private or concrete.
 *
 * @param environment [SymbolProcessorEnvironment] in which errors can be reported and files can be
 *   generated.
 */
class BuildableProcessor private constructor(private val environment: SymbolProcessorEnvironment) :
  SymbolProcessor {
  /** [SymbolProcessorProvider] that provides a [BuildableProcessor]. */
  class Provider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): BuildableProcessor {
      return BuildableProcessor(environment)
    }
  }

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val buildableDeclarations = resolver.getBuildableDeclarations()
    reportErrorOnPrivateClasses(buildableDeclarations)
    reportErrorOnConcreteClasses(buildableDeclarations)
    generateFiles(buildableDeclarations)
    return emptyList()
  }

  /**
   * Reports an error if any of the [buildableDeclarations] is private.
   *
   * @param buildableDeclarations [KSClassDeclaration]s of [Buildable]-annotated classes.
   */
  private fun reportErrorOnPrivateClasses(buildableDeclarations: Sequence<KSClassDeclaration>) {
    buildableDeclarations.filter(KSClassDeclaration::isPrivate).forEach {
      environment.logger.error(PRIVATE_CLASS_ERROR_MESSAGE, symbol = it)
    }
  }

  /**
   * Reports an error if any of the [buildableDeclarations] isn't abstract.
   *
   * @param buildableDeclarations [KSClassDeclaration]s of [Buildable]-annotated classes.
   */
  private fun reportErrorOnConcreteClasses(buildableDeclarations: Sequence<KSClassDeclaration>) {
    buildableDeclarations.filterNot(KSClassDeclaration::isAbstract).forEach {
      environment.logger.error(CONCRETE_CLASS_ERROR_MESSAGE, symbol = it)
    }
  }

  /**
   * Generates the files containing the structures that allow for each class to which the
   * [buildableDeclarations] refer to be built.
   *
   * @param buildableDeclarations [KSClassDeclaration]s of the [Buildable]-annotated classes for
   *   which the files will be generated.
   */
  private fun generateFiles(buildableDeclarations: Sequence<KSClassDeclaration>) {
    buildableDeclarations.forEach {
      val codeGenerator = environment.codeGenerator
      val file = it.requireContainingFile()
      val dependencies = Dependencies(aggregating = true, file)
      createDslMarkerAnnotatedAnnotationFileSpec(it).writeTo(codeGenerator, dependencies)
      createBuilderFileSpec(it).writeTo(codeGenerator, dependencies)
      createExtensionsFileSpec(it).writeTo(codeGenerator, dependencies)
    }
  }

  /**
   * Creates a [FileSpec] of a [DslMarker]-annotated annotation class for a [KSClassDeclaration].
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the DSL is.
   */
  private fun createDslMarkerAnnotatedAnnotationFileSpec(
    buildableClassDeclaration: KSClassDeclaration
  ): FileSpec {
    val packageName = buildableClassDeclaration.packageName.asString()
    val buildableClassName = buildableClassDeclaration.simpleName.asString()
    val annotationClassName = createDslMarkerAnnotatedAnnotationClassName(buildableClassName)
    val annotationClassSpec = createDslMarkerAnnotatedAnnotationClassSpec(buildableClassDeclaration)
    return FileSpec.builder(packageName, fileName = annotationClassName)
      .addType(annotationClassSpec)
      .build()
  }

  /**
   * Creates a [TypeSpec] of a [DslMarker]-annotated annotation class.
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the DSL is.
   */
  private fun createDslMarkerAnnotatedAnnotationClassSpec(
    buildableClassDeclaration: KSClassDeclaration
  ): TypeSpec {
    val buildableClassName = buildableClassDeclaration.simpleName.asString()
    val name = createDslMarkerAnnotatedAnnotationClassName(buildableClassName)
    val target = Target(AnnotationTarget.CLASS)
    @OptIn(DelicateKotlinPoetApi::class) val targetAnnotationSpec = AnnotationSpec.get(target)
    return TypeSpec.annotationBuilder(name)
      .addKdoc("Denotes that the annotated structure belongs to the [$buildableClassName] DSL.")
      .addModifiers(KModifier.INTERNAL)
      .addAnnotation(targetAnnotationSpec)
      .build()
  }

  /**
   * Creates a [FileSpec] of the class that builds the one to which the [buildableClassDeclaration]
   * refers.
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the builder is.
   */
  private fun createBuilderFileSpec(buildableClassDeclaration: KSClassDeclaration): FileSpec {
    val packageName = buildableClassDeclaration.packageName.asString()
    val buildableClassName = buildableClassDeclaration.simpleName.asString()
    val builderClassName = createBuilderClassName(buildableClassName)
    val file = buildableClassDeclaration.requireContainingFile()
    val classTypeSpec = createBuilderClassTypeSpec(buildableClassDeclaration)
    return FileSpec.builder(packageName, fileName = builderClassName)
      .addImports(file)
      .apply {
        buildableClassDeclaration
          .getAllSuperTypes()
          .mapNotNull { it.declaration.containingFile }
          .forEach(::addImports)
      }
      .addType(classTypeSpec)
      .build()
  }

  /**
   * Creates a class [TypeSpec] of the builder for the [buildableClassDeclaration].
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the builder is.
   */
  private fun createBuilderClassTypeSpec(buildableClassDeclaration: KSClassDeclaration): TypeSpec {
    val buildableClassName = buildableClassDeclaration.simpleName.asString()
    val buildableClassNameIndefiniteArticle = IndefiniteArticle.of(buildableClassName)
    val className = createBuilderClassName(buildableClassName)
    val dslMarkerAnnotatedAnnotationSpec =
      createDslMarkerAnnotatedAnnotationSpec(buildableClassDeclaration)
    val visibility = buildableClassDeclaration.getVisibility().toKModifier() ?: KModifier.PUBLIC
    val typeVariables =
      buildableClassDeclaration.typeParameters.toTypeParameterResolver().parametersMap.values
    val constructorPropertySpecs =
      buildableClassDeclaration.primaryConstructor?.parameters.orEmpty().map {
        createBuilderPrimaryConstructorPropertySpec(it)
      }
    val primaryConstructorFunSpec = createBuilderPrimaryConstructorFunSpec(constructorPropertySpecs)
    val memberConstructorPropertySpecs =
      constructorPropertySpecs.map { it.toBuilder().initializer(it.name).build() }
    val functionDeclarations =
      buildableClassDeclaration
        .getAllFunctions()
        .filterNot(KSFunctionDeclaration::isConstructor)
        .filter(KSFunctionDeclaration::isOpen)
        .filterNot { it.simpleName.asString() == "equals" }
        .filterNot { it.simpleName.asString() == "hashCode" }
        .filterNot { it.simpleName.asString() == "toString" }
    val callbackPropertySpecs =
      functionDeclarations.map { createBuilderCallbackPropertySpec(buildableClassDeclaration, it) }
    val callbackSetterFunSpecs =
      callbackPropertySpecs
        .mapIndexed { index, propertySpec ->
          createBuilderCallbackSetterFunSpec(
            propertySpec,
            functionDeclarations.elementAt(index).simpleName.asString()
          )
        }
        .toList()
    val buildFunSpec =
      createBuilderBuildFunSpec(
        buildableClassDeclaration,
        primaryConstructorFunSpec,
        callbackSetterFunSpecs
      )
    return TypeSpec.classBuilder(className)
      .addKdoc(
        "Allows for $buildableClassNameIndefiniteArticle [$buildableClassName] to be configured " +
          "and built."
      )
      .addAnnotation(dslMarkerAnnotatedAnnotationSpec)
      .addModifiers(visibility)
      .addTypeVariables(typeVariables)
      .primaryConstructor(primaryConstructorFunSpec)
      .apply { (memberConstructorPropertySpecs + callbackPropertySpecs).forEach(::addProperty) }
      .addFunctions(callbackSetterFunSpecs + buildFunSpec)
      .build()
  }

  /**
   * Creates an [AnnotationSpec] of the [DslMarker]-annotated annotation class that's been
   * generated.
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the DSL is.
   * @see createDslMarkerAnnotatedAnnotationClassSpec
   */
  private fun createDslMarkerAnnotatedAnnotationSpec(
    buildableClassDeclaration: KSClassDeclaration
  ): AnnotationSpec {
    val buildableClassName =
      buildableClassDeclaration.qualifiedName?.asString()
        ?: throw IllegalStateException(
          "Cannot determine $buildableClassDeclaration's qualified name."
        )
    val classNameAsString = createDslMarkerAnnotatedAnnotationClassName(buildableClassName)
    val className = ClassName.bestGuess(classNameAsString)
    return AnnotationSpec.builder(className).build()
  }

  /**
   * Creates a builder [PropertySpec] of a callback property for the [functionDeclaration].
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the builder is..
   * @param functionDeclaration [KSFunctionDeclaration] for which the [PropertySpec] is.
   */
  private fun createBuilderCallbackPropertySpec(
    buildableClassDeclaration: KSClassDeclaration,
    functionDeclaration: KSFunctionDeclaration
  ): PropertySpec {
    val buildableClassName = buildableClassDeclaration.simpleName.asString()
    val functionDeclarationName = functionDeclaration.simpleName.asString()
    val name = createBuilderCallbackPropertyName(functionDeclarationName)
    val returnType =
      functionDeclaration.returnType
        ?: throw IllegalArgumentException("A return type hasn't been specified.")
    val typeResolver = buildableClassDeclaration.typeParameters.toTypeParameterResolver()
    val parameterSpecs =
      functionDeclaration.parameters.map(::createBuilderCallbackPropertyParameterSpec)
    val returnTypeName = returnType.toTypeName(typeResolver)
    val typeName = LambdaTypeName.get(parameters = parameterSpecs, returnType = returnTypeName)
    val typeDeclarationName = returnType.resolve().declaration.simpleName.asString()
    val buildableDeclarationNamePossessiveApostrophe = PossessiveApostrophe.of(buildableClassName)
    val valueParameterDeclarationsAsString =
      parameterSpecs.ifNotEmpty { joinToString(transform = ParameterSpec::name) + " ->" }
    return PropertySpec.builder(name, typeName)
      .addKdoc(
        "Lambda that provides the [$typeDeclarationName] to be returned by the built " +
          "[$buildableClassName]$buildableDeclarationNamePossessiveApostrophe " +
          "[$functionDeclarationName][$buildableClassName.$functionDeclarationName] method."
      )
      .addModifiers(KModifier.PRIVATE)
      .mutable(true)
      .apply {
        functionDeclaration.body?.let {
          initializer(
            buildString {
              append(it, 0, 1)
              append(" $valueParameterDeclarationsAsString")
              append(it, 1, it.length)
            }
          )
        }
      }
      .build()
  }

  /**
   * Creates a [ParameterSpec] for the [valueParameter] of a callback property.
   *
   * @param valueParameter [KSValueParameter] for which the [ParameterSpec] is.
   */
  private fun createBuilderCallbackPropertyParameterSpec(
    valueParameter: KSValueParameter
  ): ParameterSpec {
    val name = valueParameter.requireName().asString()
    val typeName = valueParameter.type.toTypeName()
    return ParameterSpec.builder(name, typeName).build()
  }

  /**
   * Creates a builder [PropertySpec] of a primary constructor property for the [valueParameter].
   *
   * @param valueParameter [KSValueParameter] for which the [PropertySpec] is.
   */
  private fun createBuilderPrimaryConstructorPropertySpec(
    valueParameter: KSValueParameter
  ): PropertySpec {
    val name = valueParameter.requireName().asString()
    val typeName = valueParameter.type.toTypeName()
    return PropertySpec.builder(name, typeName)
      .addModifiers(KModifier.PRIVATE)
      .apply { valueParameter.defaultValue?.let(::initializer) }
      .build()
  }

  /**
   * Creates a [FunSpec] of the builder's primary constructor.
   *
   * @param propertySpecs [PropertySpec]s to be added as the constructor's parameters'
   *   [ParameterSpec]s.
   */
  private fun createBuilderPrimaryConstructorFunSpec(propertySpecs: List<PropertySpec>): FunSpec {
    val parameterSpecs =
      propertySpecs.map {
        ParameterSpec.builder(it.name, it.type).defaultValue(it.initializer).build()
      }
    return FunSpec.constructorBuilder()
      .addModifiers(KModifier.INTERNAL)
      .addParameters(parameterSpecs)
      .build()
  }

  /**
   * Creates a [FunSpec] of a callback property's [PropertySpec] that receives a parameter whose
   * value is the one to which the property will be set when the method is called.
   *
   * @param propertySpec [PropertySpec] of the callback property.
   * @param originalPropertyName Name of the property on which the callback one is based.
   */
  private fun createBuilderCallbackSetterFunSpec(
    propertySpec: PropertySpec,
    originalPropertyName: String
  ): FunSpec {
    return FunSpec.builder(originalPropertyName)
      .addKdoc(
        """
          Defines what will be done when [${propertySpec.name}] is invoked.

          @param $originalPropertyName Operation to be performed.
        """
          .trimIndent()
      )
      .addParameter(originalPropertyName, propertySpec.type)
      .addStatement("${propertySpec.name} = $originalPropertyName")
      .build()
  }

  /**
   * Creates a [FunSpec] for the method that builds an instance of the class to which the
   * [buildableClassDeclaration] refers to.
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the builder is.
   * @param primaryConstructorFunSpec [FunSpec] of the builder class' primary constructor.
   * @param memberFunSpecs [FunSpec]s of the member methods of the builder class.
   */
  private fun createBuilderBuildFunSpec(
    buildableClassDeclaration: KSClassDeclaration,
    primaryConstructorFunSpec: FunSpec,
    memberFunSpecs: List<FunSpec>
  ): FunSpec {
    val type = buildableClassDeclaration.asStarProjectedType()
    val simpleTypeName = type.declaration.simpleName.asString()
    val typeVariables =
      buildableClassDeclaration.typeParameters
        .toTypeParameterResolver()
        .parametersMap
        .values
        .toList()
    val typeName = type.toClassName().parameterizedOrNotBy(typeVariables)
    val anonymousClassTypeSpec =
      createAnonymousClassTypeSpec(
        buildableClassDeclaration.classKind,
        primaryConstructorFunSpec,
        typeName,
        memberFunSpecs
      )
    return FunSpec.builder(BUILDER_BUILD_METHOD_NAME)
      .addKdoc("Builds a $simpleTypeName with the provided configuration.")
      .addModifiers(KModifier.INTERNAL)
      .returns(typeName)
      .addStatement("return $anonymousClassTypeSpec")
      .build()
  }

  /**
   * Creates a [TypeSpec] of an anonymous class that's a subclass of the [Buildable]-annotated
   * class.
   *
   * @param kind [ClassKind] of the [Buildable]-annotated class.
   * @param builderPrimaryConstructorFunSpec [FunSpec] of the builder primary constructor.
   * @param typeName [TypeName] of the [Buildable]-annotated class.
   * @param memberFunSpecs [FunSpec]s of the member methods declared within the [Buildable]-annoated
   *   class.
   */
  private fun createAnonymousClassTypeSpec(
    kind: ClassKind,
    builderPrimaryConstructorFunSpec: FunSpec,
    typeName: TypeName,
    memberFunSpecs: List<FunSpec>,
  ): TypeSpec {
    return TypeSpec.anonymousClassBuilder()
      .applyIf(kind == ClassKind.INTERFACE) { addSuperinterface(typeName) }
      .applyIf(kind == ClassKind.CLASS) { superclass(typeName) }
      .apply {
        if (kind == ClassKind.CLASS) {
          builderPrimaryConstructorFunSpec.parameters
            .map(ParameterSpec::name)
            .forEach(::addSuperclassConstructorParameter)
        }
      }
      .addFunctions(memberFunSpecs)
      .build()
  }

  /**
   * Creates a [FileSpec] of the file in which the factory method for instantiating the
   * [Buildable]-annotated class is.
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the [FileSpec] is.
   */
  private fun createExtensionsFileSpec(buildableClassDeclaration: KSClassDeclaration): FileSpec {
    val packageName = buildableClassDeclaration.packageName.asString()
    val buildableClassName = buildableClassDeclaration.simpleName.asString()
    val buildableClassFile = buildableClassDeclaration.requireContainingFile()
    val factoryFunSpec = createFactoryFunSpec(buildableClassDeclaration)
    return FileSpec.builder(packageName, fileName = "$buildableClassName.extensions")
      .addImports(buildableClassFile)
      .addFunction(factoryFunSpec)
      .build()
  }

  /**
   * Creates a [FunSpec] of the factory method through which a instance of the [Buildable]-annotated
   * class can be created.
   *
   * @param buildableClassDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for
   *   which the factory is.
   */
  private fun createFactoryFunSpec(buildableClassDeclaration: KSClassDeclaration): FunSpec {
    val buildableClassName = buildableClassDeclaration.simpleName.asString()
    val builderClassName = createBuilderClassName(buildableClassName)
    val documentation = buildableClassDeclaration.docString
    val visibility = buildableClassDeclaration.getVisibility().toKModifier() ?: KModifier.PUBLIC
    val packageName = buildableClassDeclaration.packageName.asString()
    val typeParameterResolver = buildableClassDeclaration.typeParameters.toTypeParameterResolver()
    val typeVariables = typeParameterResolver.parametersMap.values.toList()
    val builderTypeName =
      ClassName(packageName, builderClassName).parameterizedOrNotBy(typeVariables)
    val buildableClassTypeName =
      buildableClassDeclaration.toClassName().parameterizedOrNotBy(typeVariables)
    val valueParameters = buildableClassDeclaration.primaryConstructor?.parameters.orEmpty()
    val parameterSpecs =
      valueParameters.map(::createFactoryParameterSpec) +
        createBuildFactoryParameterSpec(builderTypeName)
    return FunSpec.builder(buildableClassName)
      .apply { documentation?.let(::addKdoc) }
      .addModifiers(visibility)
      .addTypeVariables(typeVariables)
      .addParameters(parameterSpecs)
      .returns(buildableClassTypeName)
      .addStatement(
        "return $builderTypeName(${parameterSpecs.dropLast(1).map(ParameterSpec::name).joinToString(", ")}).apply($BUILDER_BUILD_METHOD_NAME).$BUILDER_BUILD_METHOD_NAME()"
      )
      .build()
  }

  /**
   * Creates a [ParameterSpec] of a parameter of the [Buildable]-annotated class' factory method.
   *
   * @param valueParameter [KSValueParameter] on which the created [ParameterSpec] will be based.
   */
  private fun createFactoryParameterSpec(valueParameter: KSValueParameter): ParameterSpec {
    val name = valueParameter.requireName().asString()
    val typeName = valueParameter.type.toTypeName()
    return ParameterSpec.builder(name, typeName)
      .apply { valueParameter.defaultValue?.let(::defaultValue) }
      .build()
  }

  /**
   * Creates a [ParameterSpec] of the [Buildable]-annotated class' factory method parameter that
   * configures the class to be built.
   *
   * @param builderTypeName [TypeName] of the [Buildable]-annotated class' builder.
   */
  private fun createBuildFactoryParameterSpec(builderTypeName: TypeName): ParameterSpec {
    val typeName = LambdaTypeName.get(receiver = builderTypeName, returnType = typeNameOf<Unit>())
    return ParameterSpec.builder(BUILDER_BUILD_METHOD_NAME, typeName).defaultValue("{}").build()
  }

  companion object {
    /** Error message for a [Buildable]-annotated concrete class. */
    internal const val CONCRETE_CLASS_ERROR_MESSAGE = "A concrete class cannot be buildable."

    /** Error message for a [Buildable]-annotated private class. */
    internal const val PRIVATE_CLASS_ERROR_MESSAGE = "A private class cannot be buildable."

    /**
     * Name of a builder class' method that builds an instance of its respective configured object.
     */
    internal const val BUILDER_BUILD_METHOD_NAME = "build"

    /**
     * Creates the name of the builder class for a [Buildable]-annotated class named
     * [buildableClassName].
     *
     * @param buildableClassName Name of the class to be built by the builder.
     */
    internal fun createBuilderClassName(buildableClassName: String): String {
      return buildableClassName + "Builder"
    }

    /**
     * Creates the name of a builder's callback property.
     *
     * @param methodName Name of the method for which the callback property is.
     */
    internal fun createBuilderCallbackPropertyName(methodName: String): String {
      return "on" + methodName.capitalize()
    }

    /**
     * Creates the name of the [DslMarker]-annotated annotation class.
     *
     * @param buildableClassName Name of the class for which the DSL is.
     */
    internal fun createDslMarkerAnnotatedAnnotationClassName(buildableClassName: String): String {
      return buildableClassName + "Dsl"
    }
  }
}
