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

package com.jeanbarrossilva.orca.platform.autos.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/** Material 3's [ProvidableCompositionLocal] for [Typography]. */
internal val LocalTypography
  @Composable get() = material3CompositionLocalOf(MaterialTheme.typography)

/**
 * Gets Material 3's [ProvidableCompositionLocal] for the given type [T] through reflection (since
 * it's private API). If it fails to do so, creates one that provides the [fallback].
 *
 * @param fallback Instance of [T] to fallback to and provide to the created
 *   [ProvidableCompositionLocal] if none is found at the default site.
 * @throws IllegalStateException When the name of [T] cannot be obtained.
 * @throws ClassCastException When the field at the default site exists but is not a
 *   [ProvidableCompositionLocal]<[T]>.
 */
private inline fun <reified T : Any> material3CompositionLocalOf(
  fallback: T
): ProvidableCompositionLocal<T> {
  val typeClass = T::class
  val typeName =
    typeClass.simpleName ?: throw IllegalStateException("Could not get $typeClass's name.")
  val siteClassName = "androidx.compose.material3.${typeName}Kt"
  val fieldName = "Local$typeName"

  @Suppress("UNCHECKED_CAST")
  return Class.forName(siteClassName)
    ?.getDeclaredField(fieldName)
    ?.apply { isAccessible = true }
    ?.get(null)
    ?.let { it as ProvidableCompositionLocal<T> }
    ?: staticCompositionLocalOf { fallback }
}
