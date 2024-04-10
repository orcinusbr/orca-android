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

package br.com.orcinus.orca.std.visibility.lint.detection

/**
 * Returns the receiver value if it is not `null`; otherwise, returns the result of invoking the
 * specified [defaultValue].
 *
 * @param T Object to be returned.
 * @param defaultValue Provides the value to be returned if the receiver one is `null`.
 */
internal inline fun <T> T?.orElse(defaultValue: () -> T): T {
  return this ?: defaultValue()
}
