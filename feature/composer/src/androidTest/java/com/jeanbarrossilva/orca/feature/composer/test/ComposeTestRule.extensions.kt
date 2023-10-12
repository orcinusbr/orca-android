package com.jeanbarrossilva.orca.feature.composer.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.feature.composer.COMPOSER_FIELD
import com.jeanbarrossilva.orca.feature.composer.Composer
import com.jeanbarrossilva.orca.feature.composer.ui.COMPOSER_TOOLBAR
import com.jeanbarrossilva.orca.feature.composer.ui.Toolbar

/** [SemanticsNodeInteraction] of a [Composer]'s field. */
internal fun ComposeTestRule.onField(): SemanticsNodeInteraction {
  return onNodeWithTag(COMPOSER_FIELD)
}

/** [SemanticsNodeInteraction] of a [Toolbar]. */
internal fun ComposeTestRule.onToolbar(): SemanticsNodeInteraction {
  return onNodeWithTag(COMPOSER_TOOLBAR)
}
