/*
 * Copyright © 2024 Orcinus
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

package com.jeanbarrossilva.orca.ext.coroutines

import kotlin.reflect.KProperty
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Changes the value of this [MutableStateFlow] to the given one.
 *
 * @param T Value being held.
 * @param thisRef Object in which access to the value is being performed.
 * @param property [KProperty] within the [thisRef] by which setting the value is being delegated to
 *   this method.
 * @param value Value to which this [MutableStateFlow]'s will be changed.
 * @see MutableStateFlow.value
 */
operator fun <T> MutableStateFlow<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
  this.value = value
}
