package com.jeanbarrossilva.orca.std.buildable

/**
 * Denotes that the annotated structure should be able to be built through a builder, from which
 * each one of its constructor properties can have their values set and open methods can have their
 * behavior defined.
 *
 * It allows for a more functional approach towards interfaces or abstract classes and primarily
 * aims on improving overall readability.
 *
 * For example, a given class named `MyClass`, declared as
 *
 * ```kotlin
 * @Buildable
 * abstract class MyClass(a: Int) {
 *   open fun f(b: Int) {
 *     return a * b
 *   }
 * }
 * ```
 *
 * can be instantiated by its generated factory method:
 * ```kotlin
 * MyClass(a = 64) {
 *   f { b ->
 *     a / b
 *   }
 * }
 * ```
 */
@Target(AnnotationTarget.CLASS) annotation class Buildable
