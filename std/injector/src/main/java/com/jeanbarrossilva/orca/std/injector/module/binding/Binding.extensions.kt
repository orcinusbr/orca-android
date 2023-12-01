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
