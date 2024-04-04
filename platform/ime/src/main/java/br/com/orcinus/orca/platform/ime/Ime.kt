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

package br.com.orcinus.orca.platform.ime

import androidx.annotation.IntDef
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import br.com.orcinus.orca.platform.ime.state.rememberImeAsState

/**
 * Centralizes functionality for observing the visibility of the input method editor (IME) and
 * checking what it currently is.
 *
 * A [State] whose value represents the up-to-date, current visibility can be obtained via
 * [rememberImeAsState].
 *
 * @param visibility One of [Visibility]'s constants that is coherent to whether the IME is being
 *   shown.
 * @see State.value
 * @see Visibility.UNKNOWN
 * @see Visibility.CLOSED
 * @see Visibility.OPEN
 */
@Immutable
@JvmInline
value class Ime private constructor(@Visibility private val visibility: Int) {
  /**
   * Whether the visibility of the IME is unknown.
   *
   * @see Unknown
   */
  val hasUnknownVisibility
    get() = this == Unknown

  /**
   * Whether the IME is closed.
   *
   * @see Closed
   */
  val isClosed
    get() = this == Closed

  /**
   * Whether the IME is open.
   *
   * @see Open
   */
  val isOpen
    get() = this == Open

  /** Description of the state in which the IME can be. */
  @IntDef(Visibility.UNKNOWN, Visibility.CLOSED, Visibility.OPEN)
  annotation class Visibility {
    companion object {
      /** Denotes that the visibility of the IME is unknown. */
      const val UNKNOWN = -1

      /** Denotes that the IME is closed. */
      const val CLOSED = 0

      /** Denotes that the IME is open. */
      const val OPEN = 1
    }
  }

  override fun toString(): String {
    return when {
      hasUnknownVisibility -> "Ime.Unknown"
      isClosed -> "Ime.Closed"
      isOpen -> "Ime.Open"
      else -> super.toString()
    }
  }

  companion object {
    /**
     * [Ime] with an unknown visibility.
     *
     * @see Visibility.UNKNOWN
     */
    @Stable val Unknown = Ime(Visibility.UNKNOWN)

    /**
     * [Ime] with a closed visibility.
     *
     * @see Visibility.CLOSED
     */
    @Stable val Closed = Ime(Visibility.CLOSED)

    /**
     * [Ime] with an open visibility.
     *
     * @see Visibility.OPEN
     */
    @Stable val Open = Ime(Visibility.OPEN)

    /**
     * Constant from [WindowInsetsCompat.Type] that is used for determining whether the IME is
     * visible.
     */
    @InsetsType @JvmField val type = WindowInsetsCompat.Type.ime()

    /**
     * Obtains the visibility of the IME through the [windowInsets].
     *
     * @param windowInsets [WindowInsetsCompat] that will check whether the IME is visible.
     */
    @JvmName("from")
    @JvmStatic
    internal fun from(windowInsets: WindowInsetsCompat?): Ime {
      return when (windowInsets?.isVisible(type)) {
        false -> Closed
        true -> Open
        null -> Unknown
      }
    }
  }
}
