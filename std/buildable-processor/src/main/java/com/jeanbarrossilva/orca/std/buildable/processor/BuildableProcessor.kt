package com.jeanbarrossilva.orca.std.buildable.processor

import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.isAbstract
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
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.jeanbarrossilva.orca.ext.processing.addImports
import com.jeanbarrossilva.orca.ext.processing.requireContainingFile
import com.jeanbarrossilva.orca.std.buildable.Buildable
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
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import com.squareup.kotlinpoet.typeNameOf

/**
 * [SymbolProcessor] that ensures the integrity of [Buildable]-annotated classes, generating their
 * builders or reporting an error if they're either private or concrete.
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
      val packageName = it.packageName.asString()
      val name = it.simpleName.asString()
      val dslMarkerAnnotationClassName = name + "Dsl"
      val file = it.requireContainingFile()
      val dependencies = Dependencies(aggregating = true, file)
      val codeGenerator = environment.codeGenerator
      val visibility = it.getVisibility().toKModifier() ?: KModifier.PUBLIC
      val builderClassName = name + "Builder"
      val valueParameters = it.primaryConstructor?.parameters.orEmpty()
      createDslMarkerAnnotationFileSpec(packageName, name, dslMarkerAnnotationClassName)
        .writeTo(codeGenerator, dependencies)
      createBuilderFileSpec(dslMarkerAnnotationClassName, file, it, builderClassName)
        .writeTo(codeGenerator, dependencies)
      createExtensionsFileSpec(
          file,
          it.docString,
          visibility,
          name,
          valueParameters,
          builderClassName,
          packageName
        )
        .writeTo(codeGenerator, dependencies)
    }
  }

  /**
   * Creates a [FileSpec] of a [DslMarker]-annotated annotation class for a [KSClassDeclaration].
   *
   * @param packageName Name of the package in which the [Buildable]-annotated class'
   *   [KSClassDeclaration] is.
   * @param buildableDeclarationName Name of the [Buildable]-annotated class' [KSClassDeclaration].
   * @param annotationClassName Name of the annotation class.
   */
  private fun createDslMarkerAnnotationFileSpec(
    packageName: String,
    buildableDeclarationName: String,
    annotationClassName: String
  ): FileSpec {
    val annotationClassSpec =
      createDslMarkerAnnotationClassSpec(buildableDeclarationName, annotationClassName)
    return FileSpec.builder(packageName, fileName = annotationClassName)
      .addType(annotationClassSpec)
      .build()
  }

  /**
   * Creates a [TypeSpec] of a [DslMarker]-annotated annotation class named [annotationClassName].
   *
   * @param buildableDeclarationName Name of the [Buildable]-annotated class' [KSClassDeclaration].
   * @param annotationClassName Name of the annotation class.
   */
  private fun createDslMarkerAnnotationClassSpec(
    buildableDeclarationName: String,
    annotationClassName: String
  ): TypeSpec {
    val target = Target(AnnotationTarget.CLASS)
    @OptIn(DelicateKotlinPoetApi::class) val targetAnnotationSpec = AnnotationSpec.get(target)
    return TypeSpec.annotationBuilder(annotationClassName)
      .addKdoc(
        "Denotes that the annotated structure belongs to the [$buildableDeclarationName] DSL."
      )
      .addModifiers(KModifier.INTERNAL)
      .addAnnotation(targetAnnotationSpec)
      .build()
  }

  /**
   * Creates a [FileSpec] of the class that builds the one to which the [buildableDeclaration]
   * refers.
   *
   * @param dslMarkerAnnotationClassName Name of the generated [DslMarker]-annotated class.
   * @param file [KSFile] in which the [buildableDeclaration] is.
   * @param buildableDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for which
   *   the builder is.
   * @param builderClassName Name of the builder class.
   */
  private fun createBuilderFileSpec(
    dslMarkerAnnotationClassName: String,
    file: KSFile,
    buildableDeclaration: KSClassDeclaration,
    builderClassName: String
  ): FileSpec {
    val packageName = buildableDeclaration.packageName.asString()
    val buildableDeclarationName = buildableDeclaration.simpleName.asString()
    val classTypeSpec =
      createBuilderClassTypeSpec(
        packageName,
        builderClassName,
        buildableDeclaration,
        buildableDeclarationName,
        dslMarkerAnnotationClassName
      )
    return FileSpec.builder(packageName, fileName = builderClassName)
      .addImports(file)
      .addType(classTypeSpec)
      .build()
  }

  /**
   * Creates a class [TypeSpec] of the builder for the [buildableDeclaration].
   *
   * @param packageName Name of the package in which the class will be created.
   * @param className Name of the class.
   * @param buildableDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for which
   *   the builder is.
   * @param buildableDeclarationName Name of the [buildableDeclaration].
   * @param dslMarkerAnnotationClassName Name of the generated [DslMarker]-annotated annotation
   *   class.
   */
  private fun createBuilderClassTypeSpec(
    packageName: String,
    className: String,
    buildableDeclaration: KSClassDeclaration,
    buildableDeclarationName: String,
    dslMarkerAnnotationClassName: String
  ): TypeSpec {
    val dslMarkerAnnotationSpec =
      createDslMarkerAnnotationSpec(packageName, dslMarkerAnnotationClassName)
    val visibility = buildableDeclaration.getVisibility().toKModifier() ?: KModifier.PUBLIC
    val constructorPropertySpecs =
      buildableDeclaration.primaryConstructor?.parameters.orEmpty().map {
        createBuilderPrimaryConstructorPropertySpec(it)
      }
    val primaryConstructorFunSpec = createBuilderPrimaryConstructorFunSpec(constructorPropertySpecs)
    val memberConstructorPropertySpecs =
      constructorPropertySpecs.map { it.toBuilder().initializer(it.name).build() }
    val functionDeclarations = buildableDeclaration.getAllFunctions()
    val callbackPropertySpecs =
      functionDeclarations
        .filter {
          it.isOpen() &&
            it.simpleName.asString() != "equals" &&
            it.simpleName.asString() != "hashCode" &&
            it.simpleName.asString() != "toString"
        }
        .map { createBuilderCallbackPropertySpec(buildableDeclarationName, it) }
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
        buildableDeclaration,
        primaryConstructorFunSpec,
        callbackSetterFunSpecs
      )
    return TypeSpec.classBuilder(className)
      .addAnnotation(dslMarkerAnnotationSpec)
      .addModifiers(visibility)
      .primaryConstructor(primaryConstructorFunSpec)
      .apply { (memberConstructorPropertySpecs + callbackPropertySpecs).forEach(::addProperty) }
      .addFunctions(callbackSetterFunSpecs + buildFunSpec)
      .build()
  }

  /**
   * Creates an [AnnotationSpec] of the [DslMarker]-annotated annotation class that's been
   * generated.
   *
   * @param packageName Name of the package in which the annotation class is.
   * @param annotationClassName Name of the annotation class.
   * @see createDslMarkerAnnotationClassSpec
   */
  private fun createDslMarkerAnnotationSpec(
    packageName: String,
    annotationClassName: String
  ): AnnotationSpec {
    val className = ClassName(packageName, annotationClassName)
    return AnnotationSpec.builder(className).build()
  }

  /**
   * Creates a builder [PropertySpec] of a callback property for the [functionDeclaration].
   *
   * @param buildableDeclarationName Name of the [Buildable]-annotated class' [KSClassDeclaration].
   * @param functionDeclaration [KSFunctionDeclaration] for which the [PropertySpec] is.
   */
  private fun createBuilderCallbackPropertySpec(
    buildableDeclarationName: String,
    functionDeclaration: KSFunctionDeclaration
  ): PropertySpec {
    val functionDeclarationName = functionDeclaration.simpleName.asString()
    val name = "on" + functionDeclarationName.capitalize()
    val returnType =
      functionDeclaration.returnType
        ?: throw IllegalArgumentException("A return type hasn't been specified.")
    val returnTypeName = returnType.toTypeName()
    val typeName = LambdaTypeName.get(returnType = returnTypeName)
    val typeDeclarationName = returnType.resolve().declaration.simpleName.asString()
    val buildableDeclarationNamePossessiveApostrophe =
      PossessiveApostrophe.of(buildableDeclarationName)
    return PropertySpec.builder(name, typeName)
      .addKdoc(
        "Lambda that provides the [$typeDeclarationName] to be returned by the built " +
          "[$buildableDeclarationName]$buildableDeclarationNamePossessiveApostrophe " +
          "[$functionDeclarationName][$buildableDeclarationName.$functionDeclarationName] method."
      )
      .addModifiers(KModifier.PRIVATE)
      .mutable(true)
      .apply { functionDeclaration.body?.let(::initializer) }
      .build()
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
      .addParameter(originalPropertyName, propertySpec.type)
      .addStatement("${propertySpec.name} = $originalPropertyName")
      .build()
  }

  /**
   * Creates a [FunSpec] for the method that builds an instance of the class to which the
   * [buildableDeclaration] refers to.
   *
   * @param buildableDeclaration [KSClassDeclaration] of the [Buildable]-annotated class for which
   *   the builder is.
   * @param primaryConstructorFunSpec [FunSpec] of the builder class' primary constructor.
   * @param memberFunSpecs [FunSpec]s of the member methods of the builder class.
   */
  private fun createBuilderBuildFunSpec(
    buildableDeclaration: KSClassDeclaration,
    primaryConstructorFunSpec: FunSpec,
    memberFunSpecs: List<FunSpec>
  ): FunSpec {
    val type = buildableDeclaration.asStarProjectedType()
    val simpleTypeName = type.declaration.simpleName.asString()
    val typeName = type.toTypeName()
    val anonymousClassTypeSpec =
      createAnonymousClassTypeSpec(
        buildableDeclaration.classKind,
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
      .superclass(typeName)
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
   * @param buildableAnnotatedClassFile [KSFile] in which the [Buildable]-annotated class is.
   * @param buildableAnnotatedClassDocumentation Documentation of the [Buildable]-annotated class.
   * @param buildableAnnotatedClassVisibility [KModifier] that's the visibility of the
   *   [Buildable]-annotated class.
   * @param buildableAnnotatedClassName Name of the [Buildable]-annotated class.
   * @param buildableAnnotatedClassValueParameters [KSValueParameter]s with which the
   *   [Buildable]-annotated class should be constructed.
   * @param builderClassName Name of the builder class of the [Buildable]-annotated class.
   * @param packageName Name of the package in which the method is.
   * @param buildableAnnotatedClassDocumentation Documentation of the factory method.
   */
  private fun createExtensionsFileSpec(
    buildableAnnotatedClassFile: KSFile,
    buildableAnnotatedClassDocumentation: String?,
    buildableAnnotatedClassVisibility: KModifier,
    buildableAnnotatedClassName: String,
    buildableAnnotatedClassValueParameters: List<KSValueParameter>,
    builderClassName: String,
    packageName: String,
  ): FileSpec {
    val factoryFunSpec =
      createFactoryFunSpec(
        packageName,
        buildableAnnotatedClassDocumentation,
        buildableAnnotatedClassVisibility,
        buildableAnnotatedClassName,
        builderClassName,
        buildableAnnotatedClassValueParameters
      )
    return FileSpec.builder(packageName, fileName = "$buildableAnnotatedClassName.extensions")
      .addImports(buildableAnnotatedClassFile)
      .addFunction(factoryFunSpec)
      .build()
  }

  /**
   * Creates a [FunSpec] of the factory method through which a instance of the [Buildable]-annotated
   * class can be created.
   *
   * @param packageName Name of the package in which the factory method is.
   * @param documentation Documentation of the factory method.
   * @param visibility [KModifier] that defines the visibility of the factory method.
   * @param name Name of the factory method.
   * @param builderClassName Name of the builder class of the [Buildable]-annotated class.
   * @param valueParameters [KSValueParameter]s with which the [Buildable]-annotated class should be
   *   constructed.
   */
  private fun createFactoryFunSpec(
    packageName: String,
    documentation: String?,
    visibility: KModifier,
    name: String,
    builderClassName: String,
    valueParameters: List<KSValueParameter>
  ): FunSpec {
    val buildableAnnotatedClassName = ClassName(packageName, name)
    val builderClassNameAsClassName = ClassName(packageName, builderClassName)
    val parameterSpecs =
      valueParameters.map(::createFactoryParameterSpec) +
        createBuildFactoryParameterSpec(builderClassNameAsClassName)
    return FunSpec.builder(name)
      .apply { documentation?.let(::addKdoc) }
      .addModifiers(visibility)
      .addParameters(parameterSpecs)
      .returns(buildableAnnotatedClassName)
      .addStatement(
        "return $builderClassName(${parameterSpecs.dropLast(1).map(ParameterSpec::name).joinToString(", ")}).apply($BUILDER_BUILD_METHOD_NAME).$BUILDER_BUILD_METHOD_NAME()"
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
   * @param builderClassName [ClassName] of the [Buildable]-annotated class.
   */
  private fun createBuildFactoryParameterSpec(builderClassName: ClassName): ParameterSpec {
    val typeName = LambdaTypeName.get(receiver = builderClassName, returnType = typeNameOf<Unit>())
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
  }
}
