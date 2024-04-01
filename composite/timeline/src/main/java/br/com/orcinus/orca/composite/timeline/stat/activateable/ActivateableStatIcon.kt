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

package br.com.orcinus.orca.composite.timeline.stat.activateable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import br.com.orcinus.orca.composite.timeline.stat.Stat
import br.com.orcinus.orca.composite.timeline.stat.StatDefaults
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies an [ActivateableStatIconDefaults] for testing purposes. */
internal const val ACTIVATEABLE_STAT_ICON_TAG = "activateable-stat-icon"

/** Determines whether a [ActivateableStatIconDefaults] is interactive. */
internal sealed class ActivateableStatIconInteractiveness {
  /** Mode of non-interactivity. */
  data object Still : ActivateableStatIconInteractiveness() {
    override fun onInteraction(isActive: Boolean) {}
  }

  /**
   * Mode of interactivity in which the [ActivateableStatIconDefaults] can be toggled.
   *
   * @param onInteraction Callback run whenever it's clicked, signalling a request for the state
   *   this [ActivateableStatIconDefaults] represents to be switched.
   */
  class Interactive(private val onInteraction: (isActive: Boolean) -> Unit) :
    ActivateableStatIconInteractiveness() {
    override fun onInteraction(isActive: Boolean) {
      onInteraction.invoke(isActive)
    }
  }

  /**
   * Performs an action based on the received interaction.
   *
   * @param isActive Whether the [ActivateableStatIconDefaults] is active.
   */
  internal abstract fun onInteraction(isActive: Boolean)
}

/**
 * Provides [Color]s for coloring a [ActivateableStatIconDefaults].
 *
 * @param inactiveColor [Color] by which the [ActivateableStatIconDefaults] is colored when it's
 *   inactive.
 * @param activeColor [Color] by which the [ActivateableStatIconDefaults] is colored when it's
 *   active.
 */
@Immutable
class ActivateableStatIconColors
internal constructor(private val inactiveColor: Color, private val activeColor: Color) {
  /**
   * Provides the [Color] that matches the activeness of the [ActivateableStatIconDefaults]
   * represented by [isActive].
   *
   * @param isActive Whether the [ActivateableStatIconDefaults] is active.
   */
  internal fun color(isActive: Boolean): Color {
    return if (isActive) activeColor else inactiveColor
  }
}

/**
 * [Icon] of a [Stat] that can be in an active state.
 *
 * @param modifier [Modifier] to be applied to the underlying [Icon].
 * @param isActive Whether the state it represents is enabled.
 * @param interactiveness [ActivateableStatIconInteractiveness] that indicates whether it can be
 *   interacted with.
 */
@Composable
internal fun ActivateableStatIcon(
  modifier: Modifier = Modifier,
  isActive: Boolean = false,
  interactiveness: ActivateableStatIconInteractiveness = ActivateableStatIconInteractiveness.Still
) {
  ActivateableStatIcon(
    AutosTheme.iconography.link.asImageVector,
    contentDescription = "Disconnect",
    isActive,
    interactiveness,
    ActivateableStatIconColors(
      inactiveColor = LocalContentColor.current,
      activeColor = AutosTheme.colors.link.asColor
    ),
    modifier
  )
}

/**
 * [Icon] of a [Stat] that can be in an active state.
 *
 * @param vector [ImageVector] that's the icon to be displayed.
 * @param contentDescription Shortly describes the operation it represents.
 * @param isActive Whether the state it represents is enabled.
 * @param interactiveness [ActivateableStatIconInteractiveness] that indicates whether it can be
 *   interacted with.
 * @param colors [ActivateableStatIconColors] that defines the [Color]s to color it.
 * @param modifier [Modifier] to be applied to the underlying [Icon].
 */
@Composable
internal fun ActivateableStatIcon(
  vector: ImageVector,
  contentDescription: String,
  isActive: Boolean,
  interactiveness: ActivateableStatIconInteractiveness,
  colors: ActivateableStatIconColors,
  modifier: Modifier = Modifier
) {
  var isPressed by remember { mutableStateOf(false) }
  val scale by
    animateFloatAsState(
      if (isPressed) 1.5f else 1f,
      spring(Spring.DampingRatioMediumBouncy),
      label = "Scale"
    )
  val tint by animateColorAsState(colors.color(isActive), label = "Tint")

  Icon(
    vector,
    contentDescription,
    modifier
      .pointerInput(isActive) {
        detectTapGestures(
          onPress = {
            isPressed = true
            tryAwaitRelease()
            isPressed = false
          }
        ) {
          interactiveness.onInteraction(!isActive)
        }
      }
      .scale(scale)
      .size(StatDefaults.IconSize)
      .testTag(ACTIVATEABLE_STAT_ICON_TAG)
      .semantics { selected = isActive },
    tint
  )
}

/** Preview of an inactive [ActivateableStatIcon]. */
@Composable
@MultiThemePreview
private fun InactiveActivateableStatIconPreview() {
  AutosTheme { ActivateableStatIcon(isActive = false) }
}

/** Preview of an active [ActivateableStatIcon]. */
@Composable
@MultiThemePreview
private fun ActiveActivateableStatIconPreview() {
  AutosTheme { ActivateableStatIcon(isActive = true) }
}
