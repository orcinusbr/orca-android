package com.jeanbarrossilva.orca.feature.composer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.FormatItalic
import androidx.compose.material.icons.rounded.FormatUnderlined
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.autos.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

internal const val COMPOSER_TOOLBAR = "composer-toolbar"
internal const val COMPOSER_TOOLBAR_BOLD_FORMAT = "composer-toolbar-bold-format"
internal const val COMPOSER_TOOLBAR_ITALIC_FORMAT = "composer-toolbar-italic-format"
internal const val COMPOSER_TOOLBAR_UNDERLINE_FORMAT = "composer-toolbar-underline-format"

@Composable
internal fun Toolbar(
  isBold: Boolean,
  onBoldToggle: (isBold: Boolean) -> Unit,
  isItalicized: Boolean,
  onItalicToggle: (isItalicized: Boolean) -> Unit,
  isUnderlined: Boolean,
  onUnderlineToggle: (isUnderlined: Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val shape = AutosTheme.forms.large.asShape
  val spacing = AutosTheme.spacings.small.dp

  CompositionLocalProvider(LocalContentColor provides AutosTheme.colors.surface.content.asColor) {
    LazyRow(
      modifier
        .shadow(4.dp, shape)
        .clip(shape)
        .background(AutosTheme.colors.surface.container.asColor)
        .height(56.dp)
        .testTag(COMPOSER_TOOLBAR),
      horizontalArrangement = Arrangement.spacedBy(spacing),
      verticalAlignment = Alignment.CenterVertically,
      contentPadding = PaddingValues(horizontal = spacing)
    ) {
      item {
        FormatIconButton(
          isEnabled = isBold,
          onClick = { onBoldToggle(!isBold) },
          Modifier.testTag(COMPOSER_TOOLBAR_BOLD_FORMAT)
        ) {
          Icon(Icons.Rounded.FormatBold, contentDescription = "Bold")
        }
      }

      item {
        FormatIconButton(
          isEnabled = isItalicized,
          onClick = { onItalicToggle(!isItalicized) },
          Modifier.testTag(COMPOSER_TOOLBAR_ITALIC_FORMAT)
        ) {
          Icon(Icons.Rounded.FormatItalic, contentDescription = "Italic")
        }
      }

      item {
        FormatIconButton(
          isEnabled = isUnderlined,
          onClick = { onUnderlineToggle(!isUnderlined) },
          Modifier.testTag(COMPOSER_TOOLBAR_UNDERLINE_FORMAT)
        ) {
          Icon(Icons.Rounded.FormatUnderlined, contentDescription = "Underline")
        }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun ToolbarPreview() {
  AutosTheme {
    Toolbar(
      isBold = true,
      onBoldToggle = {},
      isItalicized = false,
      onItalicToggle = {},
      isUnderlined = false,
      onUnderlineToggle = {}
    )
  }
}
