package com.jeanbarrossilva.orca.platform.theme.kit.input.option

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
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.orca.autos.forms.Forms
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.theme.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.theme.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.theme.extensions.border
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.list.Options

/** Tag that identifies an [Option] for testing purposes. */
internal const val OPTION_TAG = "option"

/**
 * Tag that identifies the [Icon] of an [Option] that indicates that it's selected for testing
 * purposes.
 */
internal const val OPTION_SELECTION_ICON_TAG = "option-selection-icon"

/** Default values of an [Option]. */
internal object OptionDefaults {
  /** [Modifier] that's applied to an [Option] by default. */
  val modifier
    @Composable get() = Modifier.border(shape)

  /** [CornerBasedShape] that clips an [Option] by default. */
  val shape
    @Composable get() = getShape(OrcaTheme.forms)

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
  val spacing = OrcaTheme.spacings.large.dp
  val selectedContainerColor = OrcaTheme.colors.primary.container.asColor
  val containerColor =
    if (isSelected) selectedContainerColor else OrcaTheme.colors.surface.container.asColor
  val contentColor = contentColorFor(containerColor)

  Row(
    modifier
      .clip(shape)
      .clickable(role = Role.Checkbox) { onSelectionToggle(!isSelected) }
      .background(containerColor)
      .padding(spacing)
      .fillMaxWidth()
      .testTag(OPTION_TAG)
      .semantics { selected = isSelected },
    Arrangement.SpaceBetween,
    Alignment.CenterVertically
  ) {
    CompositionLocalProvider(
      LocalContentColor provides contentColor,
      LocalTextStyle provides OrcaTheme.typography.labelLarge.copy(color = contentColor)
    ) {
      content()
      Spacer(Modifier.width(spacing))

      Box(Modifier.size(24.dp)) {
        if (isSelected) {
          Icon(
            OrcaTheme.iconography.selected.asImageVector,
            contentDescription = "Selected",
            Modifier.testTag(OPTION_SELECTION_ICON_TAG)
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
  OrcaTheme { Option() }
}

/** Preview of an unselected [Option]. */
@Composable
@MultiThemePreview
private fun UnselectedOptionPreview() {
  OrcaTheme { Option(isSelected = false) }
}

/** Preview of a selected [Option]. */
@Composable
@MultiThemePreview
private fun SelectedOptionPreview() {
  OrcaTheme { Option(isSelected = true) }
}
