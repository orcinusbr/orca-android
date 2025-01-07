/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.feature.composer

import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.orcinus.orca.composite.timeline.avatar.SmallAvatar
import br.com.orcinus.orca.composite.timeline.composition.CompositionTextField
import br.com.orcinus.orca.composite.timeline.composition.interop.CompositionTextFieldValue
import br.com.orcinus.orca.composite.timeline.composition.interop.drawableStateOf
import br.com.orcinus.orca.composite.timeline.composition.interop.proxy
import br.com.orcinus.orca.feature.composer.ui.Toolbar
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.kit.scaffold.plus
import br.com.orcinus.orca.platform.autos.overlays.asPaddingValues
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style

@Composable
internal fun Composer(
  viewModel: ComposerViewModel,
  onBackwardsNavigation: () -> Unit,
  modifier: Modifier = Modifier
) {
  Composer(
    onTextChange = viewModel::setText,
    onCompose = viewModel::compose,
    onBackwardsNavigation,
    modifier
  )
}

@Composable
private fun Composer(
  onTextChange: (text: Markdown) -> Unit,
  onCompose: () -> Unit,
  onBackwardsNavigation: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current
  val textField = remember(context) { CompositionTextField(context) }
  val textFieldSpacing = with(density) { CompositionTextField.getSpacing(context).toDp() }
  val textFieldStartCompoundDrawable by drawableStateOf { SmallAvatar() }
  var textFieldValue by remember { mutableStateOf(CompositionTextFieldValue.Empty) }
  var toolbarPadding by remember {
    mutableStateOf(
      PaddingValues(
        start = textFieldSpacing,
        top = textFieldSpacing,
        end = 56.dp + 16.dp,
        bottom = textFieldSpacing
      )
    )
  }

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
          toolbarPadding =
            toolbarPadding.copy(
              layoutDirection,
              end =
                with(density) {
                  (it.parentCoordinates?.size?.width?.toFloat() ?: 0f)
                    .minus(it.positionInParent().x)
                    .toDp()
                }
            )
        }
      ) {
        Icon(
          AutosTheme.iconography.send.asImageVector,
          contentDescription = stringResource(R.string.feature_composer_send)
        )
      }
    },
    FabPosition.End
  ) {
    expanded {
      Box(Modifier.fillMaxSize()) {
        LazyColumn(
          contentPadding =
            PaddingValues(bottom = textFieldSpacing) + AutosTheme.overlays.fab.asPaddingValues
        ) {
          item {
            AndroidView(
              {
                textField.also(CompositionTextField::requestFocus).apply {
                  setHint(R.string.feature_composer_hint)
                  setOnValueChangeListener { value ->
                    textFieldValue = value
                    onTextChange(value.text)
                  }
                  setImeActionLabel(null, EditorInfo.IME_ACTION_SEND)
                  setOnEditorActionListener { _, _, _ ->
                    onCompose()
                    true
                  }
                }
              },
              Modifier.fillMaxWidth().proxy(textField),
              onRelease = {
                it.setOnEditorActionListener(null)
                it.setOnValueChangeListener(null)
              }
            ) {
              it.setCompoundDrawablesRelativeWithIntrinsicBounds(
                textFieldStartCompoundDrawable,
                null,
                null,
                null
              )
              it.setValue(textFieldValue)
            }
          }
        }

        Toolbar(
          textFieldValue.isSelected<Style.Bold>(),
          onBoldToggle = { textFieldValue = textFieldValue.toggle(Style::Bold) },
          textFieldValue.isSelected<Style.Italic>(),
          onItalicToggle = { textFieldValue = textFieldValue.toggle(Style::Italic) },
          Modifier.padding(toolbarPadding).align(Alignment.BottomStart)
        )
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun ComposerPreview() {
  AutosTheme { Composer(onTextChange = {}, onCompose = {}, onBackwardsNavigation = {}) }
}
