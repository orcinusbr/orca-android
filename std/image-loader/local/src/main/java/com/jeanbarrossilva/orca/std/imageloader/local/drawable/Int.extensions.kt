/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.std.imageloader.local.drawable

/**
 * Scales this base [Int] (considered to be the adjacent one) based on the given [pb] and [ps].
 *
 * @param pb Base that's parallel to this one.
 * @param ps Scaled [Int] that's parallel to the one to be returned.
 * @return Scaled [Int] parallel to [ps].
 */
internal fun Int.scale(pb: Int, ps: Int): Int {
  return (toDouble() * (ps.toDouble() / pb.toDouble())).toInt()
}
