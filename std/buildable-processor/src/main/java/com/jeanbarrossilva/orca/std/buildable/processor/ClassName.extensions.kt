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

package com.jeanbarrossilva.orca.std.buildable.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty

/**
 * Parameterizes this [ClassName] if [typeNames] isn't empty.
 *
 * @param typeNames [TypeName]s to parameterize this [ClassName] with.
 */
internal fun ClassName.parameterizedOrNotBy(typeNames: List<TypeName>): TypeName {
  return typeNames.ifNotEmpty { parameterizedBy(typeNames) } ?: this
}
