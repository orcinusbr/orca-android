/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
