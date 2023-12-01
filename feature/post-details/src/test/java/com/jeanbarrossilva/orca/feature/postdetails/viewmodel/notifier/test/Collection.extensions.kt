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

package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * Returns the [KFunction] whose parameters' [KClass]es match the given ones.
 *
 * @param T Value returned by each of the [KFunction]s.
 */
internal operator fun <T> Collection<KFunction<T>>.get(
  vararg parameterClasses: KClass<*>
): KFunction<T> {
  return first { function ->
    function.parameters.map { parameter -> parameter.type.classifier } == parameterClasses.toList()
  }
}
