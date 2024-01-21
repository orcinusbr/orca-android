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

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.jeanbarrossilva.orca.platform.animator.Animator
import com.jeanbarrossilva.orca.platform.animator.animation.timing.after
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.SecondaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import kotlin.time.Duration.Companion.seconds

internal const val NEXT_BUTTON_TAG = "next-button"
internal const val SKIP_BUTTON_TAG = "skip-button"

@Composable
internal fun Onboarding(modifier: Modifier = Modifier) {
  Onboarding(onNext = {}, onSkip = {}, modifier)
}

@Composable
internal fun Onboarding(onNext: () -> Unit, onSkip: () -> Unit, modifier: Modifier = Modifier) {
  val fadeInSpec = tween<Float>(durationMillis = 1_000)
  val fadeIn = fadeIn(fadeInSpec)

  Animator { (appName, slogan, buttonBar) ->
    Scaffold(
      modifier,
      buttonBar = {
        buttonBar.Animate(slideInVertically { it }, after(slogan) + 2.seconds) {
          ButtonBar {
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
        contentDescription = "",
        Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )

      Column(
        Modifier.padding(top = 64.dp).fillMaxWidth(),
        Arrangement.spacedBy(AutosTheme.spacings.small.dp),
        Alignment.CenterHorizontally
      ) {
        ProvideTextStyle(LocalTextStyle.current.copy(color = Color.Black)) {
          appName.Animate(fadeIn + slideInVertically { -it }) {
            Text(
              stringResource(R.string.feature_onboarding_app_name).uppercase(),
              fontSize = 64.sp,
              fontWeight = FontWeight.Light,
              textAlign = TextAlign.Center
            )
          }

          slogan.Animate(fadeIn + slideInVertically { -it }, after(appName) + 1.seconds) {
            Text(
              stringResource(R.string.feature_onboarding_slogan),
              color = AutosTheme.colors.secondary.asColor,
              fontSize = 18.sp,
              fontWeight = FontWeight.Light
            )
          }
        }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun OnboardingPreview() {
  AutosTheme { Onboarding() }
}
