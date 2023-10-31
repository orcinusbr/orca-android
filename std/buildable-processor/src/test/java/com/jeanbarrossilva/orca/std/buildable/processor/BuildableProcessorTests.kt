package com.jeanbarrossilva.orca.std.buildable.processor

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.std.buildable.processor.test.process
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KTypeProjection
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
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          private abstract class MyClass
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
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          class MyClass
        """
      )
    assertThat(BuildableProcessor.process(file).messages)
      .contains(BuildableProcessor.CONCRETE_CLASS_ERROR_MESSAGE)
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesDslMarkerAnnotatedAnnotationClass() {
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
          .loadClass(BuildableProcessor.createDslMarkerAnnotatedAnnotationClassName("MyClass"))
          .isAnnotation
      )
      .isTrue()
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
  fun generatesBuilderWhoseFileImportsMatchThoseOfBuildableAnnotatedSubclassSuperclass() {
    val superclassFile =
      SourceFile.kotlin(
        "MySuperclass.kt",
        """
          import java.math.BigDecimal

          abstract class MySuperclass(count: BigDecimal = BigDecimal.ZERO)
        """
      )
    val subclassFile =
      SourceFile.kotlin(
        "MySubclass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MySubclass : MySuperclass()
        """
      )
    assertThat(BuildableProcessor.process(superclassFile, subclassFile).exitCode)
      .isEqualTo(KotlinCompilation.ExitCode.OK)
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
        BuildableProcessor.process(file)
          .classLoader
          .loadClass(BuildableProcessor.createBuilderClassName("MyClass"))
          .kotlin
          .visibility
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
        .loadClass(BuildableProcessor.createBuilderClassName("MyClass"))
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
        .loadClass(BuildableProcessor.createBuilderClassName("MyClass"))
        .kotlin
        .declaredMemberProperties
        .single()
    assertThat(callbackProperty).isInstanceOf<KMutableProperty1<*, *>>()
    assertThat(callbackProperty.visibility).isEqualTo(KVisibility.PRIVATE)
    assertThat(callbackProperty.returnType.arguments.last().type).isEqualTo(typeOf<Int>())
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderCallbackPropertyWithValueParametersForBuildableAnnotatedClass() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MyClass(private val count: Int) {
            open fun multiplyCountBy(factor: Int): Int {
              return count * factor
            }
          }
        """
      )
    assertThat(
        BuildableProcessor.process(file)
          .classLoader
          .loadClass(BuildableProcessor.createBuilderClassName("MyClass"))
          .kotlin
          .declaredMemberProperties
          .single {
            it.name == BuildableProcessor.createBuilderCallbackPropertyName("multiplyCountBy")
          }
          .returnType
          .arguments
          .map(KTypeProjection::type)
      )
      .containsExactly(typeOf<Int>(), typeOf<Int>())
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderCallbackPropertyOfSuperclassMethodForBuildableAnnotatedSubclass() {
    val superclassFile =
      SourceFile.kotlin(
        "MySuperclass.kt",
        """
          abstract class MySuperclass {
            open fun count(): Int {
              return 0
            }
          }
        """
      )
    val subclassFile =
      SourceFile.kotlin(
        "MySubclass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MySubclass : MySuperclass()
        """
      )
    val callbackProperty =
      BuildableProcessor.process(superclassFile, subclassFile)
        .classLoader
        .loadClass(BuildableProcessor.createBuilderClassName("MySubclass"))
        .kotlin
        .declaredMemberProperties
        .single()
    assertThat(callbackProperty).isInstanceOf<KMutableProperty1<*, *>>()
    assertThat(callbackProperty.visibility).isEqualTo(KVisibility.PRIVATE)
    assertThat(callbackProperty.returnType.arguments.last().type).isEqualTo(typeOf<Int>())
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderCallbackPropertyOfMethodOfBuildableAnnotatedSuperclassForBuildableAnnotatedSubclass() {
    val superclassFile =
      SourceFile.kotlin(
        "MySuperclass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MySuperclass {
            open fun count(): Int {
              return 0
            }
          }
        """
      )
    val subclassFile =
      SourceFile.kotlin(
        "MySubclass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MySubclass : MySuperclass()
        """
      )
    val callbackProperty =
      BuildableProcessor.process(superclassFile, subclassFile)
        .classLoader
        .loadClass(BuildableProcessor.createBuilderClassName("MySubclass"))
        .kotlin
        .declaredMemberProperties
        .single()
    assertThat(callbackProperty).isInstanceOf<KMutableProperty1<*, *>>()
    assertThat(callbackProperty.visibility).isEqualTo(KVisibility.PRIVATE)
    assertThat(callbackProperty.returnType.arguments.last().type).isEqualTo(typeOf<Int>())
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderCallbackPropertySetterOfSuperclassMethodForBuildableAnnotatedSubclass() {
    val superclassFile =
      SourceFile.kotlin(
        "MySuperclass.kt",
        """
          abstract class MySuperclass {
            open fun count(): Int {
              return 0
            }
          }
        """
      )
    val subclassFile =
      SourceFile.kotlin(
        "MySubclass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MySubclass : MySuperclass()
        """
      )
    val callbackPropertySetterMethod =
      BuildableProcessor.process(superclassFile, subclassFile)
        .classLoader
        .loadClass(BuildableProcessor.createBuilderClassName("MySubclass"))
        .declaredMethods
        .single { it.name == "count" }
    assertThat(callbackPropertySetterMethod.parameters.single().type)
      .isEqualTo(Function0::class.java)
    assertThat(callbackPropertySetterMethod.returnType).isEqualTo(Void.TYPE)
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderCallbackPropertySetterOfMethodOfBuildableAnnotatedSuperclassForBuildableAnnotatedSubclass() {
    val superclassFile =
      SourceFile.kotlin(
        "MySuperclass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MySuperclass {
            open fun count(): Int {
              return 0
            }
          }
        """
      )
    val subclassFile =
      SourceFile.kotlin(
        "MySubclass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MySubclass : MySuperclass()
        """
      )
    val callbackPropertySetterMethod =
      BuildableProcessor.process(superclassFile, subclassFile)
        .classLoader
        .loadClass(BuildableProcessor.createBuilderClassName("MySubclass"))
        .declaredMethods
        .single { it.name == "count" }
    assertThat(callbackPropertySetterMethod.parameters.single().type)
      .isEqualTo(Function0::class.java)
    assertThat(callbackPropertySetterMethod.returnType).isEqualTo(Void.TYPE)
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderCallbackPropertySetterMethodWhoseReturnStatementRequiresReferenceImportedBySuperclassForBuildableAnnotatedSubclass() {
    val superclassFile =
      SourceFile.kotlin(
        "MySuperclass.kt",
        """
          import java.math.BigDecimal

          abstract class MySuperclass {
            open fun count(): BigDecimal {
              return BigDecimal.ZERO
            }
          }
        """
      )
    val subclassFile =
      SourceFile.kotlin(
        "MySubclass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MySubclass : MySuperclass()
        """
      )
    val callbackPropertySetterMethod =
      BuildableProcessor.process(superclassFile, subclassFile)
        .classLoader
        .loadClass(BuildableProcessor.createBuilderClassName("MySubclass"))
        .declaredMethods
        .single { it.name == "count" }
    assertThat(callbackPropertySetterMethod.parameters.single().type)
      .isEqualTo(Function0::class.java)
    assertThat(callbackPropertySetterMethod.returnType).isEqualTo(Void.TYPE)
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
        .loadClass(BuildableProcessor.createBuilderClassName("MyClass"))
        .kotlin
        .declaredMemberFunctions
        .first { it.name == BuildableProcessor.BUILDER_BUILD_METHOD_NAME }
    assertThat(buildFunction.visibility).isEqualTo(KVisibility.INTERNAL)
    assertThat((buildFunction.returnType.classifier as KClass<*>).qualifiedName)
      .isEqualTo("MyClass")
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesBuilderForTypeParameterizedBuildableAnnotatedClass() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          abstract class MyClass<T>
        """
      )
    val builderClass =
      BuildableProcessor.process(file)
        .classLoader
        .loadClass(BuildableProcessor.createBuilderClassName("MyClass"))
    val buildMethod =
      builderClass.declaredMethods.single {
        it.name == "${BuildableProcessor.BUILDER_BUILD_METHOD_NAME}\$main"
      }
    assertThat(builderClass.typeParameters.single().name).isEqualTo("T")
    assertThat(buildMethod.returnType.name).isEqualTo("MyClass")
    assertThat(buildMethod.returnType.typeParameters.single().name).isEqualTo("T")
  }

  @OptIn(ExperimentalCompilerApi::class)
  @Test
  fun generatesFactoryMethodForBuildableAnnotatedInterface() {
    val file =
      SourceFile.kotlin(
        "MyClass.kt",
        """
          import com.jeanbarrossilva.orca.std.buildable.Buildable

          @Buildable
          interface MyClass
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
