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

package com.jeanbarrossilva.orca.feature.onboarding.ui.test

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import com.jeanbarrossilva.orca.feature.onboarding.NEXT_BUTTON_TAG
import com.jeanbarrossilva.orca.feature.onboarding.Onboarding
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.SecondaryButton

/** [SemanticsMatcher] that matches an [Onboarding]'s next [PrimaryButton]. */
internal fun isNextButton(): SemanticsMatcher {
  return hasTestTag(NEXT_BUTTON_TAG)
}

/** [SemanticsMatcher] that matches an [Onboarding]'s skip [SecondaryButton]. */
internal fun isSkipButton(): SemanticsMatcher {
  return hasTestTag(NEXT_BUTTON_TAG)
}
