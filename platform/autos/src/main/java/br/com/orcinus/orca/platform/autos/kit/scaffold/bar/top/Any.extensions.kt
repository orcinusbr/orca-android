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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top

/**
 * Returns the result of the given [transform] if the [condition] is `true`; otherwise, returns the
 * receiver.
 *
 * @param condition Determines whether or not the result of [transform] will get returned.
 * @param transform Transformation to be made to the receiver.
 */
inline fun <T> T.`if`(condition: Boolean, transform: T.() -> T): T {
  return if (condition) transform() else this
}
