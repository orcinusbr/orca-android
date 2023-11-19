package com.jeanbarrossilva.orca.feature.composer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.createSample
import com.jeanbarrossilva.orca.feature.composer.ui.Toolbar
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.extensions.plus
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.HoverableIconButton
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.TextFieldDefaults as _TextFieldDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.autos.overlays.asPaddingValues
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.platform.ui.core.requestFocusWithDelay
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

internal const val COMPOSER_FIELD = "composer-field"

@Composable
internal fun Composer(
  viewModel: ComposerViewModel,
  onBackwardsNavigation: () -> Unit,
  modifier: Modifier = Modifier
) {
  val value by viewModel.textFieldValueFlow.collectAsState()

  Composer(
    value,
    onValueChange = viewModel::setTextFieldValue,
    onCompose = viewModel::compose,
    onBackwardsNavigation,
    modifier
  )
}

@Composable
private fun Composer(
  value: TextFieldValue,
  onValueChange: (value: TextFieldValue) -> Unit,
  onCompose: () -> Unit,
  onBackwardsNavigation: () -> Unit,
  modifier: Modifier = Modifier,
  isToolbarInitiallyVisible: Boolean = false
) {
  val density = LocalDensity.current
  val focusRequester = remember(::FocusRequester)
  val style = AutosTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal)
  val interactionSource = remember(::MutableInteractionSource)
  val brushColor = AutosTheme.colors.primary.container.asColor
  val cursorBrush = remember(brushColor) { SolidColor(brushColor) }
  var isToolbarVisible by remember { mutableStateOf(isToolbarInitiallyVisible) }
  val toolbarSpacing = AutosTheme.spacings.medium.dp
  var toolbarSafeAreaPadding by remember { mutableStateOf(PaddingValues(end = 56.dp + 16.dp)) }
  val floatingActionButtonPosition =
    remember(isToolbarVisible) { if (isToolbarVisible) FabPosition.End else FabPosition.Center }

  LaunchedEffect(Unit) { focusRequester.requestFocusWithDelay() }

  Scaffold(
    modifier,
    topAppBar = {
      @OptIn(ExperimentalMaterial3Api::class)
      TopAppBar(
        title = { AutoSizeText(stringResource(R.string.feature_composer)) },
        navigationIcon = {
          HoverableIconButton(onClick = onBackwardsNavigation) {
            Icon(AutosTheme.iconography.back.asImageVector, contentDescription = "Back")
          }
        }
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = onCompose,
        Modifier.onPlaced {
          toolbarSafeAreaPadding =
            with(density) {
              PaddingValues(
                end =
                  (it.parentCoordinates?.size?.width?.toFloat() ?: 0f)
                    .minus(it.positionInParent().x)
                    .toDp()
              )
            }
        }
      ) {
        Icon(
          AutosTheme.iconography.send.asImageVector,
          contentDescription = stringResource(R.string.feature_composer_send)
        )
      }
    },
    floatingActionButtonPosition = floatingActionButtonPosition
  ) { padding ->
    Box(Modifier.padding(padding).fillMaxSize()) {
      LazyColumn(
        contentPadding =
          PaddingValues(AutosTheme.spacings.large.dp) + AutosTheme.overlays.fab.asPaddingValues
      ) {
        item {
          BasicTextField(
            value,
            onValueChange,
            Modifier.onFocusChanged { isToolbarVisible = it.isFocused }
              .focusRequester(focusRequester)
              .testTag(COMPOSER_FIELD),
            textStyle = style,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush
          ) { innerTextField ->
            @OptIn(ExperimentalMaterial3Api::class)
            TextFieldDefaults.DecorationBox(
              value.text,
              innerTextField,
              enabled = true,
              singleLine = false,
              VisualTransformation.None,
              interactionSource,
              placeholder = {
                Text(
                  stringResource(R.string.feature_composer_placeholder),
                  style = style.copy(color = AutosTheme.colors.tertiary.asColor)
                )
              },
              colors = _TextFieldDefaults.colors(enabledContainerColor = Color.Transparent),
              contentPadding = PaddingValues(0.dp)
            )
          }
        }
      }

      AnimatedVisibility(
        isToolbarVisible,
        Modifier.padding(start = toolbarSpacing, end = toolbarSpacing, bottom = toolbarSpacing)
          .align(Alignment.BottomStart),
        enter = slideInVertically { height -> height },
        exit = fadeOut() + slideOutVertically { height -> height }
      ) {
        /*
         * There is a bug where the EditProcessor used by CoreTextField's state under the
         * hood creates a new instance of the AnnotatedString held by the TextFieldValue
         * (whenever selection changes) only with its text property set, ignoring any span
         * or paragraph styles given in the previous recomposition.
         *
         * This is an issue known since 2019, so I don't know how likely it is that the
         * Compose team is going to fix it any time soon. Multi-style text editing has been
         * on the roadmap since October 2022.
         *
         * https://issuetracker.google.com/issues/199754661
         * https://developer.android.com/jetpack/androidx/compose-roadmap
         */
        Toolbar(
          value.isSelectionBold,
          onBoldToggle = { isBold -> onValueChange(value.withBoldSelection(isBold)) },
          value.isSelectionItalicized,
          onItalicToggle = { isItalicized ->
            onValueChange(value.withItalicizedSelection(isItalicized))
          },
          value.isSelectionUnderlined,
          onUnderlineToggle = { isUnderlined ->
            onValueChange(value.withUnderlinedSelection(isUnderlined))
          },
          Modifier.padding(toolbarSafeAreaPadding)
        )
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun EmptyWithoutToolbarComposerPreview() {
  AutosTheme { Composer(TextFieldValue(AnnotatedString("")), isInitiallyFocused = false) }
}

@Composable
@MultiThemePreview
private fun EmptyWithToolbarComposerPreview() {
  AutosTheme { Composer(TextFieldValue(AnnotatedString("")), isInitiallyFocused = true) }
}

@Composable
@MultiThemePreview
private fun PopulatedWithoutToolbarComposerPreview() {
  AutosTheme {
    Composer(
      TextFieldValue(
        Toot.createSample(ImageLoader.Provider.createSample()).content.text.toAnnotatedString()
      ),
      isInitiallyFocused = false
    )
  }
}

@Composable
@MultiThemePreview
private fun PopulatedWithToolbarComposerPreview() {
  AutosTheme {
    Composer(
      TextFieldValue(
        Toot.createSample(ImageLoader.Provider.createSample()).content.text.toAnnotatedString()
      ),
      isInitiallyFocused = true
    )
  }
}

@Composable
private fun Composer(
  value: TextFieldValue,
  isInitiallyFocused: Boolean,
  modifier: Modifier = Modifier
) {
  Composer(
    value,
    onValueChange = {},
    onCompose = {},
    onBackwardsNavigation = {},
    modifier,
    isInitiallyFocused
  )
}
