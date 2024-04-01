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

package com.jeanbarrossilva.orca.platform.focus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

/**
 * [Duration] of the delay of an immediate [FocusRequester]'s initial focus.
 *
 * @see rememberImmediateFocusRequester
 */
internal val ImmediateFocusRequesterInitialDelay = 256.milliseconds

/** Remembers a [FocusRequester] that automatically requests initial focus. */
@Composable
fun rememberImmediateFocusRequester(): FocusRequester {
  return remember(::FocusRequester).also {
    LaunchedEffect(it) {
      delay(ImmediateFocusRequesterInitialDelay)
      it.requestFocus()
    }
  }
}
