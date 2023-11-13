package com.jeanbarrossilva.orca.platform.ui.component.timeline.bottom

import androidx.annotation.RestrictTo
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/** Tag that identifies a [RenderEffect] for testing purposes. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX) const val RENDER_EFFECT_TAG = "render-effect"

/**
 * Adds a [RenderEffect] to this [LazyListScope].
 *
 * @param contentType Content type to be associated to the [RenderEffect] for Compose to be able to
 *   reuse it.
 * @param onEffect Callback run when the [RenderEffect] is rendered.
 */
internal fun LazyListScope.renderEffect(contentType: Any? = null, onEffect: () -> Unit) {
  item(key = "render-effect", contentType) { RenderEffect { onEffect() } }
}

/**
 * [Composable] that acts both as a marker and a trigger for the bottom of a [Timeline], running the
 * given [effect] for notifying when it's been reached.
 *
 * @param effect Operation to be performed once this [Composable] is rendered for the first time.
 */
@Composable
private fun RenderEffect(effect: () -> Unit) {
  var isFirstRender by rememberSaveable { mutableStateOf(true) }

  LaunchedEffect(Unit) {
    if (isFirstRender) {
      effect()
      isFirstRender = false
    }
  }

  Spacer(Modifier.testTag(RENDER_EFFECT_TAG))
}
