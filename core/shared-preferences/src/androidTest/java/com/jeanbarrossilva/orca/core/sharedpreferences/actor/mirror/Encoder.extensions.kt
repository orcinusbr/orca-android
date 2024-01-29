/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.sharedpreferences.actor.mirror

import com.jeanbarrossilva.orca.ext.reflection.access
import kotlin.reflect.full.primaryConstructor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Creates an instance of a [kotlinx.serialization.json.internal.JsonTreeEncoder] reflectively.
 *
 * @throws NoSuchMethodException If the primary constructor isn't found.
 */
@Throws(NoSuchMethodException::class)
internal fun createJsonTreeEncoder(): Encoder {
  return Class.forName("kotlinx.serialization.json.internal.JsonTreeEncoder")
    .kotlin
    .primaryConstructor
    ?.access { call(Json, { _: JsonElement -> }) as Encoder }
    ?: throw NoSuchMethodException("JsonTreeEncoder.<init>(Json, Function1<JsonElement, Unit>)")
}
