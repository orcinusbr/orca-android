package com.jeanbarrossilva.orca.std.injector.module.binding

import com.jeanbarrossilva.orca.std.injector.module.Module

/**
 * Creates a [Binding] between this [Module]'s actual type and the specified base one.
 *
 * @param B Base [Module] to which this [Module] will be bound to.
 * @param A [Module] at the utmost bottom of the inheritance tree to which the [target] will be
 *   bound to.
 */
inline fun <reified B : Module, reified A : B> A.boundTo(): Binding<B, A> {
  return Binding(B::class, A::class, this)
}
