/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.autos.kit.sheet.controller

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue

/** [SheetState] returned by [SheetState.Companion.AlwaysHidden]. */
@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
private val alwaysHiddenSheetState =
  SheetState(
    skipPartiallyExpanded = false,
    initialValue = SheetValue.Hidden,
    skipHiddenState = false
  )

/** An always-hidden [SheetState]. */
@OptIn(ExperimentalMaterial3Api::class)
internal val SheetState.Companion.AlwaysHidden
  get() = alwaysHiddenSheetState
