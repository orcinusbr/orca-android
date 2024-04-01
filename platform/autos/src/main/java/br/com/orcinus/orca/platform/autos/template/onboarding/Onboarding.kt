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

package br.com.orcinus.orca.platform.autos.template.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.PrimaryButton
import br.com.orcinus.orca.platform.autos.kit.action.button.SecondaryButton
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.plus
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Type of content contained by an [Onboarding]. */
@Immutable
private enum class OnboardingContentType {
  /** Type of an [Onboarding] illustration. */
  Illustration,

  /** Type of an [Onboarding] headline. */
  Headline,

  /**
   * Type of the empty content of an [Onboarding] used to separate the illustration from the
   * headline.
   *
   * @see Illustration
   * @see Headline
   */
  Spacer
}

/**
 * Template for introducing a feature.
 *
 * @param illustration Visual representation that facilitates the understanding of what the [title]
 *   and the [description] explain.
 * @param title Exposes the overall idea, generally by resuming what the feature does or the outcome
 *   of using it for the user.
 * @param description Provides more details on the subject being presented, expanding on what the
 *   [title] says.
 * @param modifier [Modifier] for the underlying [LazyColumn].
 * @param contentPadding Padding to be applied to the shown content.
 */
@Composable
fun Onboarding(
  illustration: @Composable () -> Unit,
  title: @Composable () -> Unit,
  description: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(0.dp)
) {
  val spacing = AutosTheme.spacings.large.dp

  LazyColumn(
    modifier.fillMaxHeight(),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally,
    contentPadding = contentPadding + PaddingValues(spacing)
  ) {
    item(contentType = OnboardingContentType.Spacer) {}
    item(contentType = OnboardingContentType.Illustration) { illustration() }
    item(contentType = OnboardingContentType.Spacer) {}

    item(contentType = OnboardingContentType.Headline) {
      Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
        ProvideTextStyle(AutosTheme.typography.headlineLarge, title)
        ProvideTextStyle(AutosTheme.typography.headlineSmall, description)
      }
    }
  }
}

/** Preview of an [Onboarding]. */
@Composable
@MultiThemePreview
private fun OnboardingPreview() {
  AutosTheme {
    val leadingBackdropColor = AutosTheme.colors.activation.favorite.asColor
    val trailingBackdropColor = AutosTheme.colors.activation.reposted.asColor

    Scaffold(
      bottom = {
        ButtonBar {
          PrimaryButton(onClick = {}) { Text("Continue") }
          SecondaryButton(onClick = {}) { Text("Go back") }
        }
      }
    ) {
      expanded {
        Onboarding(
          illustration = {
            Box(contentAlignment = Alignment.Center) {
              Canvas(Modifier.matchParentSize()) {
                drawCircle(leadingBackdropColor, center = center / 2f)
                drawCircle(trailingBackdropColor, center = center * 1.5f)
              }

              Icon(
                AutosTheme.iconography.home.filled.asImageVector,
                contentDescription = "Illustration",
                Modifier.size(128.dp)
              )
            }
          },
          title = { Text("Greatest feature of all") },
          description = {
            Text(
              "And then here goes a very detailed description of it: what it is, what and who it " +
                "is for, what it does..."
            )
          },
          contentPadding = it
        )
      }
    }
  }
}
