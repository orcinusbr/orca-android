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

package com.jeanbarrossilva.orca.platform.animator

import androidx.compose.animation.EnterTransition
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.platform.animator.animatable.Animatable
import com.jeanbarrossilva.orca.platform.animator.animatable.Animatables
import com.jeanbarrossilva.orca.platform.animator.animatable.timing.after
import com.jeanbarrossilva.orca.platform.animator.animatable.timing.immediately

/**
 * Animator is an Orca-specific animation API built on top of Jetpack Compose's that facilitates the
 * orchestration of sequential [EnterTransition]s that depend on others for them to start running.
 *
 * It works by providing [Animatable]s (not to be confused with
 * [androidx.compose.animation.Animatable]) to the given [content], each intended to arbitrarily
 * refer to a [Composable] that will have its visibility toggled on alongside the execution of an
 * [EnterTransition].
 *
 * [Composable]s can have their entrance animated by being provided as the content of their assigned
 * [Animatable]'s [Animatable.Animate]. For example:
 * ```kotlin
 * Animator { (greeting) ->
 *   greeting.Animate(fadeIn()) {
 *     Text("Hello, world!")
 *   }
 * }
 * ```
 *
 * In this case, the greeting would fade in [immediately], since it doesn't depend on other
 * animations and no posterior delay has been specified. Similarly, the following is how it would be
 * done if the greeting was to be displayed only 2 seconds [after] another [Composable]'s animation
 * has finished running:
 * ```kotlin
 * Animator { (emoji, greeting) ->
 *   emoji.Animate(fadeIn()) {
 *     Text("🌎")
 *   }
 *
 *   greeting.Animate(fadeIn(), after(emoji) + 2.seconds) {
 *     Text("Hello, world!")
 *   }
 * }
 * ```
 *
 * Note that this [Composable], [Animator], serves merely as an entrypoint to the overall API and
 * doesn't have any intrinsic behavior other than just showing the [content] and providing an
 * instance of [Animatables] to it.
 *
 * @param content Content to be shown in which [Animatable]-based animations can be run.
 */
@Composable
fun Animator(content: @Composable (Animatables) -> Unit) {
  content(Animatables.instance)
}
