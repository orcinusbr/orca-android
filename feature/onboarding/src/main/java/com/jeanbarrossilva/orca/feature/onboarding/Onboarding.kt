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

package com.jeanbarrossilva.orca.feature.onboarding

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.platform.animator.Animator
import com.jeanbarrossilva.orca.platform.animator.animation.timing.after
import com.jeanbarrossilva.orca.platform.animator.animation.timing.immediately
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.SecondaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBarMaterial
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import kotlin.time.Duration.Companion.seconds

internal const val NEXT_BUTTON_TAG = "next-button"
internal const val SKIP_BUTTON_TAG = "skip-button"

private object OnboardingDefaults {
  private const val AnimationDurationInMilliseconds = 1_500

  fun <T> animationSpec(): FiniteAnimationSpec<T> {
    return tween(AnimationDurationInMilliseconds)
  }
}

@Composable
internal fun Onboarding(modifier: Modifier = Modifier) {
  Onboarding(onNext = {}, onSkip = {}, modifier)
}

@Composable
internal fun Onboarding(onNext: () -> Unit, onSkip: () -> Unit, modifier: Modifier = Modifier) {
  val buttonBarHazeState = remember(::HazeState)
  val buttonBarContainerColor = remember { Colors.DARK.background.container.asColor }
  val slideInVertically = slideInVertically(OnboardingDefaults.animationSpec()) { it }
  val fadeIn = fadeIn(OnboardingDefaults.animationSpec())

  Animator { (appName, buttonBar) ->
    Scaffold(
      modifier,
      buttonBar = {
        buttonBar.Animate(slideInVertically, after(appName) + 2.seconds) {
          ButtonBar(
            material = ButtonBarMaterial.Vibrant(buttonBarHazeState),
            containerColor = buttonBarContainerColor
          ) {
            PrimaryButton(onClick = onNext, Modifier.testTag(NEXT_BUTTON_TAG)) {
              Text(stringResource(R.string.feature_onboarding_next))
            }

            SecondaryButton(onClick = onSkip, Modifier.testTag(SKIP_BUTTON_TAG)) {
              Text(stringResource(R.string.feature_onboarding_skip))
            }
          }
        }
      }
    ) {
      Image(
        painterResource(R.drawable.hero),
        contentDescription = stringResource(R.string.feature_onboarding_hero),
        Modifier.haze(buttonBarHazeState, buttonBarContainerColor).fillMaxSize(),
        contentScale = ContentScale.Crop
      )

      appName.Animate(fadeIn + slideInVertically, immediately() + 1.seconds) {
        Text(
          stringResource(R.string.feature_onboarding_app_name).uppercase(),
          Modifier.padding(top = 175.dp).fillMaxWidth(),
          Color.Black,
          80.sp,
          fontWeight = FontWeight.Light,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun OnboardingPreview() {
  AutosTheme { Onboarding() }
}
