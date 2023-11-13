package com.jeanbarrossilva.orca.platform.ui.component.timeline.bottom

import androidx.annotation.RestrictTo
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/** Tag that identifies a [RenderEffect] for testing purposes. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX) const val RENDER_EFFECT_TAG = "render-effect"

/**
 * Adds a [RenderEffect] to this [LazyListScope].
 *
 * @param key Value to which a change indicates whether the effect should be reset and its next
 *   rendering should be taken into account. Note that this isn't an identifier for the
 *   [RenderEffect] item to be added.
 * @param contentType Content type to be associated to the [RenderEffect] for Compose to be able to
 *   reuse it.
 * @param onEffect Callback run when the [RenderEffect] is rendered.
 */
internal fun LazyListScope.renderEffect(
  key: Any? = null,
  contentType: Any? = null,
  onEffect: () -> Unit
) {
  item(key = "render-effect", contentType) { RenderEffect(key) { onEffect() } }
}

/**
 * [Composable] that acts both as a marker and a trigger for the bottom of a [Timeline], running the
 * given [effect] for notifying when it's been reached.
 *
 * @param key Value to which a change indicate that the [effect] should be run.
 * @param effect Operation to be performed once this [Composable] is rendered for the first time and
 *   when the [key] changes.
 */
@Composable
private fun RenderEffect(key: Any?, effect: () -> Unit) {
  DisposableEffect(key) {
    effect()
    onDispose {}
  }

  DisposableEffect(Unit) {
    effect()
    onDispose {}
  }

  Spacer(Modifier.testTag(RENDER_EFFECT_TAG))
}
