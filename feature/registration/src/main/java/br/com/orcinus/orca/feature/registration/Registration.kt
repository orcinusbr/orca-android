/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.feature.registration

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.composite.status.Status
import br.com.orcinus.orca.composite.status.StatusCard
import br.com.orcinus.orca.composite.status.state.rememberStatusCardState
import br.com.orcinus.orca.platform.animator.Animator
import br.com.orcinus.orca.platform.animator.animation.Motion
import br.com.orcinus.orca.platform.animator.animation.timing.after
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.kit.action.button.PrimaryButton
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.plus
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.stack.Stack
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun Registration(boundary: RegistrationBoundary, modifier: Modifier = Modifier) {
  Registration(boundary, Motion.Moving, modifier)
}

@Composable
private fun Registration(
  boundary: RegistrationBoundary,
  motion: Motion,
  modifier: Modifier = Modifier
) {
  val spacing = AutosTheme.spacings.large.dp
  val spacerModifier = remember(spacing) { Modifier.height(spacing) }
  val backdropColor = AutosTheme.colors.backdrop.asColor
  val statusCardAnimationSpec = remember { tween<Float>() }
  val statusCardEnterTransition =
    remember(statusCardAnimationSpec) {
      fadeIn(statusCardAnimationSpec) + scaleIn(statusCardAnimationSpec, initialScale = .8f)
    }
  val statusCardDelay = remember { 2.seconds }

  Scaffold(
    modifier,
    bottom = {
      ButtonBar { PrimaryButton(onClick = boundary::navigateToCredentials) { Text("Continue") } }
    }
  ) {
    expanded {
      LazyColumn(
        Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = it + PaddingValues(spacing)
      ) {
        item { Spacer(spacerModifier) }

        item {
          Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(256.dp)) { drawCircle(backdropColor) }

            Animator(motion) { (failedStatusCard, succeededStatusCard) ->
              Stack {
                item {
                  failedStatusCard.Animate {
                    StatusCard(rememberStatusCardState(Status.Failed, statusCardDelay)) {
                      Text("Instance 1")
                    }
                  }
                }

                item {
                  succeededStatusCard.Animate(
                    statusCardEnterTransition,
                    after(failedStatusCard) + statusCardDelay * 1.5
                  ) {
                    StatusCard(rememberStatusCardState(Status.Succeeded, statusCardDelay)) {
                      Text("Instance 2")
                    }
                  }
                }
              }
            }
          }
        }

        item { Spacer(spacerModifier) }

        item {
          Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.large.dp)) {
            Text(
              stringResource(R.string.feature_registration),
              style = AutosTheme.typography.headlineLarge
            )

            Text(
              stringResource(R.string.feature_registration_explanation),
              style = AutosTheme.typography.headlineSmall
            )
          }
        }

        item { Spacer(spacerModifier) }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun StillRegistrationPreview() {
  AutosTheme { Registration(NoOpRegistrationBoundary, Motion.Still) }
}

@Composable
@MultiThemePreview
private fun MovingRegistrationPreview() {
  AutosTheme { Registration(NoOpRegistrationBoundary) }
}
