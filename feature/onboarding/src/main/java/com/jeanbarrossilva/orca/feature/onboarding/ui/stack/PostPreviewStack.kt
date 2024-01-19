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

package com.jeanbarrossilva.orca.feature.onboarding.ui.stack

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreviewDefaults
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

private object PostPreviewStackDefaults {
  const val Size = 5

  val VisibleDefaultElevation = 0.5.dp
}

@Composable
internal fun PostPreviewStack(
  areInitiallyVisible: Boolean,
  modifier: Modifier = Modifier,
  onAnimationEnding: () -> Unit = {}
) {
  Box(modifier) {
    repeat(PostPreviewStackDefaults.Size) {
      var isVisible by remember { mutableStateOf(areInitiallyVisible) }
      val scale by animateFloatAsState(if (isVisible) 1f else 1.2f, label = "Scale")
      val isMirrored = remember(it) { it % 2 != 0 }
      var defaultElevation by remember {
        mutableStateOf(
          if (areInitiallyVisible) PostPreviewStackDefaults.VisibleDefaultElevation else 0.dp
        )
      }
      val isLast = remember(it) { it == PostPreviewStackDefaults.Size.dec() }

      LaunchedEffect(Unit) {
        delay(512.milliseconds * it.inc())
        isVisible = true
        defaultElevation = PostPreviewStackDefaults.VisibleDefaultElevation
        if (isLast) {
          onAnimationEnding()
        }
      }

      AnimatedVisibility(
        isVisible,
        Modifier.rotate(if (isMirrored) -5f else 5f)
          .scale(scale)
          .padding(AutosTheme.spacings.large.dp),
        enter = slideInHorizontally { width -> if (isMirrored) width else -width }
      ) {
        PostPreview(
          shape = AutosTheme.forms.large.asShape,
          elevation = PostPreviewDefaults.elevation(defaultElevation)
        )
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun PostPreviewStackPreview() {
  AutosTheme { PostPreviewStack(areInitiallyVisible = true) }
}
