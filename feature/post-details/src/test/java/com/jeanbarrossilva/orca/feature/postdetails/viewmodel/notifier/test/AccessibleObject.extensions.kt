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

import kotlin.reflect.KCallable
import kotlin.reflect.jvm.isAccessible

/**
 * Makes this [KCallable] accessible for the given [access] and resets its accessibility to the
 * previous one when the [access] lambda has finished running.
 *
 * @param I [KCallable] to be accessed.
 * @param O Value returned by the [access].
 * @param access Access to be performed while this [KCallable] is ensured to be accessible.
 * @see KCallable.isAccessible
 */
internal fun <I : KCallable<*>, O> I.access(access: I.() -> O): O {
  val wasAccessible = isAccessible
  isAccessible = true
  return access().also { isAccessible = wasAccessible }
}
