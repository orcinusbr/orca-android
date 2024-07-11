/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.kit.input.option

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.autos.forms.Forms
import br.com.orcinus.orca.platform.autos.border
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.input.option.list.Options
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder

/** Tag that identifies an [Option] for testing purposes. */
internal const val OptionTag = "option"

/**
 * Tag that identifies the [Icon] of an [Option] that indicates that it's selected for testing
 * purposes.
 */
internal const val OptionSelectionIconTag = "option-selection-icon"

/** Default values of an [Option]. */
internal object OptionDefaults {
  /** [Modifier] that's applied to an [Option] by default. */
  val modifier
    @Composable get() = Modifier.border(shape)

  /** [CornerBasedShape] that clips an [Option] by default. */
  val shape
    @Composable get() = getShape(AutosTheme.forms)

  /**
   * Gets the [CornerBasedShape] that clips an [Option] by default.
   *
   * @param forms [Forms] from which the [CornerBasedShape] will be obtained.
   */
  fun getShape(forms: Forms): CornerBasedShape {
    return forms.medium.asShape
  }
}

/**
 * A loading selectable configuration.
 *
 * @param modifier [Modifier] to be applied to the underlying [Row].
 */
@Composable
fun Option(modifier: Modifier = Modifier) {
  CoreOption(OptionDefaults.modifier then modifier)
}

/**
 * A selectable configuration.
 *
 * @param isSelected Whether it is selected.
 * @param onSelectionToggle Callback run whenever it is toggled.
 * @param modifier [Modifier] to be applied to the underlying [Row].
 * @param content [Text] that's the label.
 */
@Composable
fun Option(
  isSelected: Boolean,
  onSelectionToggle: (isSelected: Boolean) -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  CoreOption(
    isSelected,
    onSelectionToggle,
    OptionDefaults.modifier then modifier,
    content = content
  )
}

/**
 * A selectable configuration.
 *
 * @param isSelected Whether it is selected.
 * @param modifier [Modifier] to be applied to the underlying [Row].
 * @param onSelectionToggle Callback run whenever it is toggled.
 */
@Composable
internal fun Option(
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onSelectionToggle: (isSelected: Boolean) -> Unit = {}
) {
  Option(isSelected, onSelectionToggle, modifier) { Text("Label") }
}

/**
 * A loading selectable configuration. This is the [Composable] used by [Option], given that it
 * allows for more customization in terms of its appearance, which, for example, makes it suitable
 * to be composed alongside other options within [Options].
 *
 * @param modifier [Modifier] to be applied to the underlying [Row].
 * @param shape [Shape] by which it will be clipped.
 */
@Composable
internal fun CoreOption(modifier: Modifier = Modifier, shape: Shape = OptionDefaults.shape) {
  CoreOption(isSelected = false, onSelectionToggle = {}, modifier, shape) {
    MediumTextualPlaceholder()
  }
}

/**
 * A selectable configuration. This is the [Composable] used by [Option], given that it allows for
 * more customization in terms of its appearance, which, for example, makes it suitable to be
 * composed alongside other options within [Options].
 *
 * @param isSelected Whether it is selected.
 * @param onSelectionToggle Callback run whenever it is toggled.
 * @param modifier [Modifier] to be applied to the underlying [Row].
 * @param shape [Shape] by which it will be clipped.
 * @param content [Text] that's the label.
 */
@Composable
internal fun CoreOption(
  isSelected: Boolean,
  onSelectionToggle: (isSelected: Boolean) -> Unit,
  modifier: Modifier = Modifier,
  shape: Shape = OptionDefaults.shape,
  content: @Composable () -> Unit
) {
  val spacing = AutosTheme.spacings.large.dp
  val selectedContainerColor = AutosTheme.colors.primary.container.asColor
  val containerColor =
    if (isSelected) selectedContainerColor else AutosTheme.colors.surface.container.asColor
  val contentColor = contentColorFor(containerColor)

  Row(
    modifier
      .clip(shape)
      .clickable(role = Role.Checkbox) { onSelectionToggle(!isSelected) }
      .background(containerColor)
      .padding(spacing)
      .fillMaxWidth()
      .testTag(OptionTag)
      .semantics { selected = isSelected },
    Arrangement.SpaceBetween,
    Alignment.CenterVertically
  ) {
    CompositionLocalProvider(
      LocalContentColor provides contentColor,
      LocalTextStyle provides AutosTheme.typography.labelLarge.copy(color = contentColor)
    ) {
      content()
      Spacer(Modifier.width(spacing))

      Box(Modifier.size(24.dp)) {
        if (isSelected) {
          Icon(
            AutosTheme.iconography.selected.asImageVector,
            contentDescription = "Selected",
            Modifier.testTag(OptionSelectionIconTag)
          )
        }
      }
    }
  }
}

/** Preview of a loading [Option]. */
@Composable
@MultiThemePreview
private fun LoadingOptionPreview() {
  AutosTheme { Option() }
}

/** Preview of an unselected [Option]. */
@Composable
@MultiThemePreview
private fun UnselectedOptionPreview() {
  AutosTheme { Option(isSelected = false) }
}

/** Preview of a selected [Option]. */
@Composable
@MultiThemePreview
private fun SelectedOptionPreview() {
  AutosTheme { Option(isSelected = true) }
}
