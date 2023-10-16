package com.jeanbarrossilva.orca.std.injector.module.binding

import com.jeanbarrossilva.orca.std.injector.module.Module
import kotlin.reflect.KClass

/** [Binding] with generic base and alias. */
internal typealias SomeBinding = Binding<*, *>

/**
 * Link between the [target], the [base] and the [alias], both from which the first can be later
 * obtained after its registration.
 *
 * @param B Base [Module] to which the [target] is bound to.
 * @param A [Module] at the utmost bottom of the inheritance tree to which the [target] is bound to.
 * @param base Base [KClass] to which the [target] is bound to.
 * @param alias [KClass] whose type is at the utmost bottom of the inheritance tree to which the
 *   [target] is bound to.
 * @param target [Module] that's bound to both the [base] and the [alias].
 */
data class Binding<B : Module, A : B>
@PublishedApi
internal constructor(val base: KClass<B>, val alias: KClass<A>, val target: A) {
  /**
   * Whether the given [KClass] is part of this [Binding].
   *
   * @param other [KClass] of a [Module] whose presence will be verified.
   */
  operator fun contains(other: KClass<out Module>): Boolean {
    return base == other || alias == other
  }
}
