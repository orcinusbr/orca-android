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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.truth

import android.view.View
import androidx.compose.runtime.Composable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Attaches this [SourceOfTruth] to both the [View] and the [Composable], performs the specified
 * operation and then detaches it afterwards.
 *
 * @param usage Action to be executed when the receiver is attached.
 */
@OptIn(ExperimentalContracts::class)
internal fun SourceOfTruth.use(usage: (SourceOfTruth) -> Unit) {
  contract { callsInPlace(usage, InvocationKind.EXACTLY_ONCE) }
  attach()
  usage(this)
  detach()
}
