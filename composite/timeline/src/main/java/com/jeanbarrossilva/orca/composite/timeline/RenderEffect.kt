/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.testTag

/** Tag that identifies a [RenderEffect] for testing purposes. */
const val RENDER_EFFECT_TAG = "render-effect"

/**
 * Adds a [RenderEffect] to this [LazyListScope].
 *
 * @param key Value to which a change indicates whether the effect should be reset and its next
 *   rendering should be taken into account. Note that this isn't an identifier for the
 *   [RenderEffect] item to be added.
 * @param contentType Content type to be associated to the [RenderEffect] for Compose to be able to
 *   reuse it.
 * @param onPlacement Lambda invoked when the [RenderEffect] is laid out.
 * @param onEffect Callback run when the [RenderEffect] is rendered.
 */
internal fun LazyListScope.renderEffect(
  key: Any,
  contentType: Any,
  onPlacement: () -> Unit,
  onEffect: () -> Unit
) {
  item(key = "render-effect", contentType) { RenderEffect(key, onPlacement) { onEffect() } }
}

/**
 * [Composable] that acts both as a marker and a trigger for the bottom of a [Timeline], running the
 * given [effect] for notifying when it's been reached.
 *
 * @param key Value to which a change indicates that the [effect] should be run.
 * @param onPlacement Lambda invoked when it's laid out.
 * @param effect Operation to be performed once this [Composable] is rendered for the first time and
 *   when the [key] changes.
 */
@Composable
private fun RenderEffect(key: Any, onPlacement: () -> Unit, effect: () -> Unit) {
  DisposableEffect(Unit) {
    effect()
    onDispose {}
  }

  DisposableEffect(key) {
    effect()
    onDispose {}
  }

  Spacer(Modifier.onPlaced { onPlacement() }.testTag(RENDER_EFFECT_TAG))
}
