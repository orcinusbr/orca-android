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

package com.jeanbarrossilva.orca.feature.registration

import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.instance.domain.samples
import com.jeanbarrossilva.orca.feature.registration.ui.stack.Stack
import com.jeanbarrossilva.orca.feature.registration.ui.status.Status
import com.jeanbarrossilva.orca.feature.registration.ui.status.StatusCard
import com.jeanbarrossilva.orca.feature.registration.ui.status.rememberStatusCardState
import com.jeanbarrossilva.orca.platform.animator.Animator
import com.jeanbarrossilva.orca.platform.animator.animation.Motion
import com.jeanbarrossilva.orca.platform.animator.animation.timing.after
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.autos.theme.Rubik
import kotlin.time.Duration.Companion.seconds

@Composable
private fun Registration(modifier: Modifier = Modifier, motion: Motion = Motion.Moving) {
  val verticalSpacing = AutosTheme.spacings.extraLarge.dp
  val statusCardEnterTransition = fadeIn() + scaleIn(initialScale = .5f)
  val statusCardDelay = remember { 2.seconds }

  Scaffold(modifier, bottom = { ButtonBar { PrimaryButton(onClick = {}) { Text("Continue") } } }) {
    expanded {
      LazyColumn(
        Modifier.padding(it).fillMaxHeight(),
        contentPadding = PaddingValues(horizontal = AutosTheme.spacings.large.dp, verticalSpacing),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        item {}

        item {
          Box(Modifier.padding(vertical = verticalSpacing).fillMaxHeight(), Alignment.Center) {
            Animator(motion) { (failedStatusCard, succeededStatusCard) ->
              Stack {
                item {
                  failedStatusCard.Animate(statusCardEnterTransition) {
                    StatusCard(rememberStatusCardState(statusCardDelay, Status.Failed)) {
                      Text("${Domain.samples[1]}")
                    }
                  }
                }

                item {
                  succeededStatusCard.Animate(
                    statusCardEnterTransition,
                    after(failedStatusCard) + statusCardDelay
                  ) {
                    StatusCard(rememberStatusCardState(statusCardDelay, Status.Succeeded)) {
                      Text("${Domain.samples[2]}")
                    }
                  }
                }
              }
            }
          }
        }

        item {
          Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraLarge.dp)) {
            Text("Create an account", style = AutosTheme.typography.headlineLarge)

            Text(
              "Orca will try to find an instance that is available and create an account for you " +
                "with the credentials you pass in. All of it in a completely secure way, not " +
                "storing any of the given private information (such as your desired password).",
              color = AutosTheme.colors.secondary.asColor,
              fontFamily = FontFamily.Rubik,
              fontSize = 18.sp,
              lineHeight = 24.sp
            )
          }
        }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun RegistrationPreview() {
  AutosTheme { Registration(motion = Motion.Moving) }
}
