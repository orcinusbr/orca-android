/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import java.io.Serializable

/** Tag that identifies a [RenderEffect] for testing purposes. */
@InternalTimelineApi const val RenderEffectTag = "render-effect"

/**
 * Key to which a [RenderEffect] is associated when it is added to a lazy list.
 *
 * @see LazyStaggeredGridScope.renderEffect
 */
@Immutable
private object RenderEffectKey : Serializable {
  /**
   * Allows the class to replace/resolve the [RenderEffectKey] read from the stream before it is
   * returned to the caller and directly control the types and instances of its own instances being
   * deserialized.
   *
   * Refer to the
   * [Java Object Serialization Specification](https://docs.oracle.com/en/java/javase/11/docs/specs/serialization/input.html#the-readresolve-method).
   */
  private fun readResolve(): Any {
    return RenderEffectKey
  }
}

/**
 * Adds a [RenderEffect] to the lazy list.
 *
 * Note that an [IllegalArgumentException] will be thrown if this is done on the same lazy list
 * multiple times, due to the fact that the [RenderEffect] item has a unique key that would get
 * reused if it got added more than once.
 *
 * @param contentType Content type to be associated to the [RenderEffect] for Compose to be able to
 *   reuse it.
 * @param keys Values to which changes indicate whether the effect should be reset and its next
 *   rendering should be taken into account. Note that these aren't identifiers for the
 *   [RenderEffect] item to be added.
 * @param effect Callback run when the [RenderEffect] is rendered.
 */
internal fun LazyStaggeredGridScope.renderEffect(
  contentType: Any,
  vararg keys: Any?,
  effect: () -> Unit
) {
  item(RenderEffectKey, contentType, StaggeredGridItemSpan.FullLine) {
    RenderEffect(*keys, effect = effect)
  }
}

/**
 * [Composable] that acts both as a marker and a trigger for the bottom of a [Timeline], running the
 * given [effect] for notifying when it's been reached.
 *
 * @param keys Values to which changes indicate that the [effect] should be run.
 * @param effect Operation to be performed once this [Composable] is rendered for the first time and
 *   when the [keys] change.
 */
@Composable
private fun RenderEffect(vararg keys: Any?, effect: () -> Unit) {
  DisposableEffect(*keys) {
    effect()
    onDispose {}
  }

  Spacer(Modifier.testTag(RenderEffectTag))
}
