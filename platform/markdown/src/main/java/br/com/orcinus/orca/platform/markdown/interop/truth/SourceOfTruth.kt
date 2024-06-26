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

package br.com.orcinus.orca.platform.markdown.interop.truth

import android.view.View
import android.widget.EditText
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Interactions between [Composable]s and the system [View]s are bidirectional by nature, given that
 * the first are declarative and composition-based and, the latter, imperative and callback-based;
 * that results in inconsistencies such as a [Composable] text field being in an invalid or unknown
 * state due to non-sanitized inputs to its backing [EditText], since the `(String) -> Unit`-like
 * on-change callback passed into it is responsible for (possibly) performing validation and
 * (probably) updating the text to be displayed (rather than it being the job of its [View]
 * counterpart).
 *
 * A source of truth aims to mitigate that very issue by validating whether the [View]'s current
 * state matches that of the [Composable] and resetting the [View]'s to that prior to the actual
 * recomposition when they're mismatching.
 *
 * For the corrections to be effectively applied, this source must be attached to both the [View]
 * and the [Composable]. When conformity enforcement is no longer necessary (i. e., when the
 * [AndroidView] gets released), it should then be detached.
 *
 * @see attach
 * @see detach
 */
internal interface SourceOfTruth {
  /**
   * Observes and corrects a [View]'s state, making it conformant to that of the [Composable].
   *
   * @see detach
   */
  fun attach()

  /**
   * Cancels [View]-to-[Composable] conformity observations and corrections.
   *
   * @see attach
   */
  fun detach()
}
