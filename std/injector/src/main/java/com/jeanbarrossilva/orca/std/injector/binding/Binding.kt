package com.jeanbarrossilva.orca.std.injector.binding

import com.jeanbarrossilva.orca.std.injector.module.Module
import kotlin.reflect.KClass

/** [Binding] with generic base and alias. */
internal typealias SomeBinding = Binding<*, *>

/**
 * Link between the the [base] and the [alias], both from which a [Module] can be later obtained
 * after its registration.
 *
 * @param B Base [Module] to which a [Module] is bound to.
 * @param A [Module] at the utmost bottom of the inheritance tree to which a [Module] is bound to.
 * @param base Base [KClass] to which a [Module] is bound to.
 * @param alias [KClass] whose type is at the utmost bottom of the inheritance tree to which a
 *   [Module] is bound to.
 */
data class Binding<B : Module, A : B>
@PublishedApi
internal constructor(val base: KClass<B>, val alias: KClass<A>) {
  /**
   * Whether the given [KClass] is part of this [Binding].
   *
   * @param other [KClass] of a [Module] whose presence will be verified.
   */
  operator fun contains(other: KClass<out Module>): Boolean {
    return base == other || alias == other
  }
}
