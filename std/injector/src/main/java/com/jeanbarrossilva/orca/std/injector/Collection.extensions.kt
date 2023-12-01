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

package com.jeanbarrossilva.orca.std.injector

import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.typeOf

/**
 * Returns a [List] containing only [KProperty1]s that characterize an injection into a [Module].
 * Those:
 * - Are annotated with [Inject], denoting that the dependencies returned by their values should be
 *   automatically injected into the [Module] in which they were declared;
 * - Have a return type of `Module.() -> Any`, which allows for operations to be performed within
 *   the [Module] and returns a dependency of any non-`null` type.
 *
 * @param T [Module] into which the resulting dependencies of the [KProperty1]s' values will be
 *   injected.
 */
@PublishedApi
internal fun <T : Module> Collection<KProperty1<T, *>>.filterIsInjection():
  List<KProperty1<T, Module.() -> Any>> {
  return filter {
      it.hasAnnotation<Inject>() &&
        it.returnType.arguments.size == 2 &&
        it.returnType.arguments.first().type == typeOf<Module>()
    }
    .castTo()
}
