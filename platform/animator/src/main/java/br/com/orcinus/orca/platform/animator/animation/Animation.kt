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

package br.com.orcinus.orca.platform.animator.animation

import androidx.compose.runtime.Immutable

/** Stage in which an animation can be. */
@Immutable
internal enum class Animation {
  /** States that an animation hasn't started running. */
  Idle,

  /** Denotes that an animation has been requested to be run but hasn't started yet. */
  Prepared,

  /** Indicates that an animation is in progress. */
  Ongoing,

  /** Represents that an animation has finished running. */
  Ended
}
