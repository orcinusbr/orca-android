package com.jeanbarrossilva.orca.std.buildable.processor

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.jeanbarrossilva.orca.std.buildable.processor.test.process
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.typeOf
import kotlin.test.Test
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

internal class BuildableProcessorTests {
  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun reportsErrorOnBuildableAnnotatedPrivateClass() {
    val file =
      SourceFile.kotlin(
        "PrivateClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          private abstract class PrivateClass
        """
      )
    assertThat(BuildableProcessor.process(file).messages)
      .contains(BuildableProcessor.PRIVATE_CLASS_ERROR_MESSAGE)
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun reportsErrorOnBuildableAnnotatedConcreteClass() {
    val file =
      SourceFile.kotlin(
        "ConcreteClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          class ConcreteClass
        """
      )
    assertThat(BuildableProcessor.process(file).messages)
      .contains(BuildableProcessor.CONCRETE_CLASS_ERROR_MESSAGE)
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderWhoseFileImportsMatchThoseOfBuildableAnnotatedClass() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable
          import java.math.BigDecimal

          @Buildable
          abstract class MyClass(count: BigDecimal = BigDecimal.ZERO)
        """
      )
    assertThat(BuildableProcessor.process(file).exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderWhoseVisibilityMatchesThatOfBuildableAnnotatedClass() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          internal abstract class MyClass
        """
      )
    assertThat(
        BuildableProcessor.process(file).classLoader.loadClass("MyClassBuilder").kotlin.visibility
      )
      .isEqualTo(KVisibility.INTERNAL)
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderPrimaryConstructorParameterForBuildableAnnotatedClass() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MyClass(count: Int = 0)
        """
      )
    val primaryConstructor =
      BuildableProcessor.process(file)
        .classLoader
        .loadClass("MyClassBuilder")
        .kotlin
        .primaryConstructor
    val primaryConstructorParameter = primaryConstructor?.parameters.orEmpty().single()
    assertThat(primaryConstructorParameter.name).isEqualTo("count")
    assertThat(primaryConstructorParameter.type).isEqualTo(typeOf<Int>())
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderCallbackPropertyForBuildableAnnotatedClass() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MyClass {
            open fun count(): Int {
                return 0
            }
          }
        """
      )
    val callbackProperty =
      BuildableProcessor.process(file)
        .classLoader
        .loadClass("MyClassBuilder")
        .kotlin
        .declaredMemberProperties
        .single()
    assertThat(callbackProperty).isInstanceOf<KMutableProperty1<*, *>>()
    assertThat(callbackProperty.visibility).isEqualTo(KVisibility.PRIVATE)
    assertThat(callbackProperty.returnType.arguments.last().type).isEqualTo(typeOf<Int>())
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderBuildMethodForBuildableAnnotatedClass() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MyClass
        """
      )
    val buildFunction =
      BuildableProcessor.process(file)
        .classLoader
        .loadClass("MyClassBuilder")
        .kotlin
        .declaredMemberFunctions
        .first { it.name == BuildableProcessor.BUILDER_BUILD_METHOD_NAME }
    assertThat(buildFunction.visibility).isEqualTo(KVisibility.INTERNAL)
    assertThat((buildFunction.returnType.classifier as KClass<*>).qualifiedName)
      .isEqualTo("MyClass")
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesFactoryMethodForBuildableAnnotatedClass() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MyClass
        """
      )
    assertThat(
        BuildableProcessor.process(file)
          .classLoader
          .loadClass("MyClass_extensionsKt")
          .getMethod("MyClass", Function1::class.java)
          .returnType
          .name
      )
      .isEqualTo("MyClass")
  }
}
