package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.annotation.RestrictTo
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.testTag

/** Tag that identifies a [RenderEffect] for testing purposes. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX) const val RENDER_EFFECT_TAG = "render-effect"

/**
 * Triggers the given [effect] lambda the first time it's rendered.
 *
 * @param effect Operation to be performed when this [Composable] is rendered.
 */
@Composable
internal fun RenderEffect(effect: () -> Unit) {
  var isFirstRender by remember { mutableStateOf(true) }
  Spacer(
    Modifier.onPlaced {
        if (isFirstRender) {
          effect()
          isFirstRender = false
        }
      }
      .testTag(RENDER_EFFECT_TAG)
  )
}
