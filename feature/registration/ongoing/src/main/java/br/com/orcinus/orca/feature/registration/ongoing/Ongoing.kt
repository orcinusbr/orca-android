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

package br.com.orcinus.orca.feature.registration.ongoing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.orcinus.orca.composite.status.Status
import br.com.orcinus.orca.composite.status.StatusCard
import br.com.orcinus.orca.composite.status.state.rememberStatusCardState
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.core.sample.instance.domain.samples
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.template.onboarding.Onboarding
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.platform.stack.Stack
import br.com.orcinus.orca.platform.stack.StackScope
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun Ongoing(viewModel: OngoingViewModel, modifier: Modifier = Modifier) {
  val indexedDomain by viewModel.indexedDomainFlow.collectAsState()
  Ongoing(indexedDomain, OngoingViewModel.perDomainDelay, modifier)
}

@Composable
private fun Ongoing(
  indexedDomain: IndexedValue<Domain>?,
  perDomainDelay: Duration,
  modifier: Modifier = Modifier
) {
  var statusCardStackScope by remember { mutableStateOf<StackScope?>(null) }
  val statusCardAnimationSpec = tween<Float>()
  val statusCardEnterTransition =
    remember(statusCardAnimationSpec) {
      fadeIn(statusCardAnimationSpec) + scaleIn(statusCardAnimationSpec, initialScale = .8f)
    }

  LaunchedEffect(indexedDomain, statusCardStackScope, statusCardEnterTransition) {
    if (statusCardStackScope != null && indexedDomain != null) {
      statusCardStackScope?.item {
        AnimatedVisibility(
          remember { MutableTransitionState(false).apply { targetState = true } },
          enter = statusCardEnterTransition
        ) {
          StatusCard(
            rememberStatusCardState(
              targetStatus =
                if (indexedDomain.index == Domain.samples.lastIndex) {
                  Status.Succeeded
                } else {
                  Status.Failed
                },
              perDomainDelay / 2
            )
          ) {
            Text("${indexedDomain.value}")
          }
        }
      }
    }
  }

  Scaffold(modifier) {
    expanded {
      Onboarding(
        illustration = { Stack { statusCardStackScope = this } },
        title = { Text(stringResource(R.string.feature_registration_ongoing)) },
        description = { Text(stringResource(R.string.feature_registration_ongoing_description)) }
      )
    }
  }
}

@Composable
@MultiThemePreview
private fun OngoingPreview() {
  AutosTheme { Ongoing(IndexedValue(0, Domain.sample), perDomainDelay = 4.seconds) }
}
