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

@file:JvmName("AccessibleObjects")

package br.com.orcinus.orca.ext.reflection.java

import java.lang.reflect.AccessibleObject

/**
 * Makes this [AccessibleObject] accessible for the given [access] and resets its accessibility to
 * the previous one when the [access] lambda has finished running.
 *
 * @param I [AccessibleObject] to be accessed.
 * @param O Value returned by the [access].
 * @param access Access to be performed while this [AccessibleObject] is ensured to be accessible.
 * @see AccessibleObject.isAccessible
 */
fun <I : AccessibleObject, O> I.access(access: I.() -> O): O {
  @Suppress("DEPRECATION") val wasAccessible = isAccessible
  isAccessible = true
  return access().also { isAccessible = wasAccessible }
}
