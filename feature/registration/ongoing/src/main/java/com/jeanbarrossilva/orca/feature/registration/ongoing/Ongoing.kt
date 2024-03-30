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

package com.jeanbarrossilva.orca.feature.registration.ongoing

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.composite.status.StatusCard
import com.jeanbarrossilva.orca.composite.status.state.rememberStatusCardState
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.instance.domain.samples
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.template.onboarding.Onboarding
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.stack.Stack
import com.jeanbarrossilva.orca.platform.stack.StackScope
import kotlin.time.Duration.Companion.seconds

@Composable
private fun Ongoing(modifier: Modifier = Modifier) {
  var statusCardStackScope by remember { mutableStateOf<StackScope?>(null) }
  val statusCardDelay = remember { 2.seconds }

  LaunchedEffect(statusCardStackScope) {
    statusCardStackScope?.let { stackScope ->
      Domain.samples.forEachIndexed { index, domain ->
        stackScope.item { StatusCard(rememberStatusCardState()) { Text("$domain") } }
      }
    }
  }

  Scaffold(modifier) {
    expanded {
      Onboarding(
        illustration = { Stack { statusCardStackScope = this } },
        title = { Text("Creating an account for you...") },
        description = {
          Text(
            "Orca is going through each of its known instances and will create your account at " +
              "one that is available."
          )
        },
        contentPadding = it
      )
    }
  }
}

@Composable
@MultiThemePreview
private fun OngoingPreview() {
  AutosTheme { Ongoing() }
}
