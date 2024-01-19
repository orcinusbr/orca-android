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

package com.jeanbarrossilva.orca.feature.onboarding.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jeanbarrossilva.orca.feature.onboarding.R
import com.jeanbarrossilva.orca.feature.onboarding.ui.stack.PostPreviewStack
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.SecondaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

internal const val NEXT_BUTTON_TAG = "next-button"
internal const val SKIP_BUTTON_TAG = "skip-button"

@Composable
internal fun Onboarding(modifier: Modifier = Modifier) {
  Onboarding(Movement.Still, onNext = {}, onSkip = {}, modifier)
}

@Composable
internal fun Onboarding(
  movement: Movement,
  onNext: () -> Unit,
  onSkip: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isButtonBarVisible by movement.rememberIsButtonBarInitiallyVisibleAsState()
  val slogan by movement.rememberSloganAsState()

  Scaffold(
    modifier,
    buttonBar = {
      AnimatedVisibility(isButtonBarVisible, enter = slideInVertically { it }) {
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
  ) { padding ->
    ConstraintLayout(modifier.padding(padding).fillMaxSize()) {
      createRefs().let { (postPreviewStackRef, headlineRef) ->
        PostPreviewStack(
          movement.arePostPreviewsInitiallyVisible,
          Modifier.padding(top = AutosTheme.spacings.large.dp).fillMaxWidth().constrainAs(
            postPreviewStackRef
          ) {},
          onAnimationEnding = { isButtonBarVisible = true }
        )

        Column(
          Modifier.fillMaxWidth().constrainAs(headlineRef) {
            top.linkTo(postPreviewStackRef.bottom)
            bottom.linkTo(parent.bottom)
          },
          Arrangement.spacedBy(AutosTheme.spacings.medium.dp),
          Alignment.CenterHorizontally
        ) {
          Text(
            stringResource(R.string.feature_onboarding_app_name),
            textAlign = TextAlign.Center,
            style = AutosTheme.typography.headlineLarge.copy(fontSize = 64.sp)
          )

          Text(slogan, textAlign = TextAlign.Center, style = AutosTheme.typography.titleSmall)
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
