/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.ext.coroutines.notifier

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
