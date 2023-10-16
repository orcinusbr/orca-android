package com.jeanbarrossilva.orca.std.injector.binding

import com.jeanbarrossilva.orca.std.injector.module.Module

/**
 * Creates a [Binding] between [A] and [B].
 *
 * @param B Base [Module] to which a [Module] is bound to.
 * @param A [Module] at the utmost bottom of the inheritance tree to which a [Module] is bound to.
 */
inline fun <reified B : Module, reified A : B> bind(): Binding<B, A> {
  return Binding(B::class, A::class)
}
