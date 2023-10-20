package com.jeanbarrossilva.orca.std.providable

/**
 * Denotes that a provider should be generated for the annotated interface, through which an
 * instance of it can be obtained.
 *
 * For example,
 * ```kotlin
 * @Providable
 * interface MyInterface {
 *   val prop: Int
 * }
 * ```
 *
 * will have a respective
 *
 * ```kotlin
 * fun interface MyInterfaceProvider {
 *   fun provide(param: Int): MyClass
 * }
 * ```
 *
 * generated for it within the same package.
 */
@Target(AnnotationTarget.CLASS) annotation class Providable
