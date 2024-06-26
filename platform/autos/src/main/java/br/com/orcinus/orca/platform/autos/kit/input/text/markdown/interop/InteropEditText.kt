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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.AppCompatEditText
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextFieldColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.EditProcessor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.MarkdownTextField

/**
 * [EditText] by which Jetpack-Compose-specific behavior is translated into that of the Android
 * [View] system.
 *
 * Exists primarily because of an issue known since 2019 and that hasn't yet been addressed as of
 * 1.6.6, which causes [androidx.compose.foundation.text.CoreTextField]'s [EditProcessor] to create
 * a new instance of the [AnnotatedString] contained by the [TextFieldValue] passed into it only
 * with its [TextFieldValue.text] property set and ignoring any [SpanStyle]s or [ParagraphStyle]s
 * provided in the previous composition.
 *
 * Multi-style text editing has been on the
 * [roadmap](https://developer.android.com/jetpack/androidx/compose-roadmap) since October 2022.
 *
 * @see AnnotatedString.spanStyles
 * @see AnnotatedString.paragraphStyles
 */
internal class InteropEditText : AppCompatEditText, KeyboardActionScope {
  /**
   * [LayoutDirectionProvider] that determines whether a compound [Drawable] is the leading or
   * trailing one based on the direction of the layout provided by it.
   */
  private var layoutDirectionProvider = LayoutDirectionProvider {
    context?.resources?.configuration?.layoutDirection ?: TEXT_DIRECTION_LTR
  }

  /** [OnSelectionChangeListener]s that are notified whenever selection changes are performed. */
  private val onSelectionChangeListeners = mutableListOf<OnSelectionChangeListener>()

  /**
   * [KeyboardActions] containing the actions to be performed whenever their respective callback is
   * called.
   *
   * @see setKeyboardActions
   */
  private var keyboardActions: KeyboardActions? = null

  /**
   * [TextFieldColors] by which this [InteropEditText] is colored.
   *
   * @see setColors
   */
  @VisibleForTesting
  var colors: TextFieldColors? = null
    private set

  /**
   * Layout-direction-based index of the compound [Drawable] at the start of this [InteropEditText].
   *
   * @see trailingCompoundDrawableIndex
   */
  @VisibleForTesting
  val leadingCompoundDrawableIndex
    get() =
      layoutDirectionProvider.provide().takeUnless(View.LAYOUT_DIRECTION_LTR::equals)?.let {
        RIGHT_COMPOUND_DRAWABLE_INDEX
      }
        ?: LEFT_COMPOUND_DRAWABLE_INDEX

  /**
   * Layout-direction-based index of the compound [Drawable] at the end of this [InteropEditText].
   *
   * @see leadingCompoundDrawableIndex
   */
  @VisibleForTesting
  val trailingCompoundDrawableIndex
    get() =
      if (leadingCompoundDrawableIndex == RIGHT_COMPOUND_DRAWABLE_INDEX) {
        LEFT_COMPOUND_DRAWABLE_INDEX
      } else {
        RIGHT_COMPOUND_DRAWABLE_INDEX
      }

  /**
   * [EditText] presented by the [MarkdownTextField] on which [TextFieldColors] can be applied.
   *
   * @param context [Context] in which the [View] will be added.
   * @see setColors
   */
  constructor(
    context: Context
  ) : this(context, attributeSet = null, defaultStyleAttributeResourceID = 0)

  /**
   * [EditText] presented by the [MarkdownTextField] on which [TextFieldColors] can be applied.
   *
   * @param context [Context] in which the [View] will be added.
   * @param attributeSet Attributes specified in the XML.
   * @see setColors
   */
  constructor(
    context: Context,
    attributeSet: AttributeSet?
  ) : this(context, attributeSet, defaultStyleAttributeResourceID = 0)

  /**
   * [EditText] presented by the [MarkdownTextField] on which [TextFieldColors] can be applied.
   *
   * @param context [Context] in which the [View] will be added.
   * @param attributeSet Attributes specified in the XML.
   * @param defaultStyleAttributeResourceID Resource ID of the attribute belonging to the default
   *   style to be set.
   * @see setColors
   */
  constructor(
    context: Context,
    attributeSet: AttributeSet?,
    @AttrRes defaultStyleAttributeResourceID: Int
  ) : super(context, attributeSet, defaultStyleAttributeResourceID)

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    colors?.colorBasedOnEnableability(enabled)
  }

  override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
    super.onFocusChanged(focused, direction, previouslyFocusedRect)
    colors?.colorBasedOnFocus(focused)
  }

  override fun onSelectionChanged(selStart: Int, selEnd: Int) {
    super.onSelectionChanged(selStart, selEnd)

    /*
     * The safe call isn't unnecessary at all; onSelectionChanged(Int, Int) is called by the system
     * before onSelectionChangeListeners has been initialized with the empty list that is assigned
     * to it.
     */
    @Suppress("UNNECESSARY_SAFE_CALL")
    onSelectionChangeListeners?.let { listeners ->
      val selection = selStart..selEnd
      listeners.forEach { listener -> listener.onSelectionChange(selection) }
    }
  }

  override fun onEditorAction(actionCode: Int) {
    super.onEditorAction(actionCode)
    keyboardActions?.let {
      when (actionCode) {
        EditorInfo.IME_ACTION_DONE -> it.onDone
        EditorInfo.IME_ACTION_GO -> it.onGo
        EditorInfo.IME_ACTION_NEXT -> it.onNext
        EditorInfo.IME_ACTION_PREVIOUS -> it.onPrevious
        EditorInfo.IME_ACTION_SEARCH -> it.onSearch
        EditorInfo.IME_ACTION_SEND -> it.onSend
        else -> null
      }?.invoke(this)
    }
  }

  override fun setBackgroundColor(@ColorInt color: Int) {
    background?.setTint(color) ?: run { background = ColorDrawable(color) }
  }

  override fun setError(error: CharSequence?, icon: Drawable?) {
    super.setError(error, icon)
    colors?.colorBasedOnError()
  }

  override fun defaultKeyboardAction(imeAction: ImeAction) {
    onEditorAction(EditorInfo.IME_ACTION_UNSPECIFIED)
  }

  /**
   * Changes the [LayoutDirectionProvider] by which a layout direction is to be provided.
   *
   * @param layoutDirectionProvider [LayoutDirectionProvider] that determines whether a compound
   *   [Drawable] is the leading or trailing one based on the direction of the layout provided by
   *   it.
   */
  @VisibleForTesting
  fun setLayoutDirectionProvider(layoutDirectionProvider: LayoutDirectionProvider) {
    this.layoutDirectionProvider = layoutDirectionProvider
  }

  /**
   * Adds an [OnSelectionChangeListener].
   *
   * @param onSelectionChangeListener [OnSelectionChangeListener] that will be notified when
   *   selection changes.
   */
  fun addOnSelectionChangeListener(onSelectionChangeListener: OnSelectionChangeListener) {
    onSelectionChangeListeners.add(onSelectionChangeListener)
  }

  /**
   * Removes an [OnSelectionChangeListener].
   *
   * @param onSelectionChangeListener [OnSelectionChangeListener] that won't be further notified of
   *   changes in selection.
   */
  fun removeOnSelectionChangeListener(onSelectionChangeListener: OnSelectionChangeListener) {
    onSelectionChangeListeners.remove(onSelectionChangeListener)
  }

  /**
   * Colors this [InteropEditText] with the given [colors].
   *
   * @param [colors] [TextFieldColors] by which this [InteropEditText] will be colored.
   */
  fun setColors(colors: TextFieldColors?) {
    this.colors =
      colors?.apply {
        when {
          error != null -> colorBasedOnError()
          isFocused -> colorBasedOnFocus(isFocused = true)
          else -> colorBasedOnEnableability(isEnabled)
        }
      }
  }

  /**
   * Defines the actions to be executed when their respective callbacks are called.
   *
   * @param keyboardActions [KeyboardActions] containing the actions to be performed.
   */
  fun setKeyboardActions(keyboardActions: KeyboardActions?) {
    this.keyboardActions = keyboardActions
  }

  /**
   * Defines the behaviors of the IME.
   *
   * @param keyboardOptions [KeyboardOptions] that dictates how the IME will function.
   */
  fun setKeyboardOptions(keyboardOptions: KeyboardOptions?) {
    keyboardOptions?.let { _ ->
      inputType =
        if (keyboardOptions.autoCorrect) {
          inputType or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
        } else {
          inputType and InputType.TYPE_TEXT_FLAG_AUTO_CORRECT.inv()
        }
      when (keyboardOptions.capitalization) {
        KeyboardCapitalization.Characters -> InputFilter.AllCaps()
        KeyboardCapitalization.Sentences -> CapitalizationInputFilter.Sentence
        KeyboardCapitalization.Words -> CapitalizationInputFilter.Word
        else -> null
      }?.let { filters += it }
      setImeActionLabel(
        null,
        when (keyboardOptions.imeAction) {
          ImeAction.Done -> EditorInfo.IME_ACTION_DONE
          ImeAction.Go -> EditorInfo.IME_ACTION_GO
          ImeAction.Next -> EditorInfo.IME_ACTION_NEXT
          ImeAction.Previous -> EditorInfo.IME_ACTION_PREVIOUS
          ImeAction.Search -> EditorInfo.IME_ACTION_SEARCH
          ImeAction.Send -> EditorInfo.IME_ACTION_SEND
          else -> EditorInfo.IME_ACTION_UNSPECIFIED
        }
      )
    }
  }

  /**
   * Applies the enableability-based [Color]s.
   *
   * @param isEnabled Whether this [InteropEditText] is currently enabled.
   */
  private fun TextFieldColors.colorBasedOnEnableability(isEnabled: Boolean) {
    color(
      when {
        isFocused -> focusedContainerColor
        isEnabled -> unfocusedContainerColor
        else -> disabledTextColor
      },
      when {
        isFocused -> focusedLeadingIconColor
        isEnabled -> unfocusedLeadingIconColor
        else -> disabledLeadingIconColor
      },
      when {
        isFocused -> focusedTrailingIconColor
        isEnabled -> unfocusedTrailingIconColor
        else -> disabledTrailingIconColor
      },
      when {
        isFocused -> focusedPlaceholderColor
        isEnabled -> unfocusedPlaceholderColor
        else -> disabledPlaceholderColor
      },
      when {
        isFocused -> focusedTextColor
        isEnabled -> unfocusedTextColor
        else -> disabledTextColor
      }
    )
  }

  /**
   * Applies the focus-based [Color]s.
   *
   * @param isFocused Whether this [InteropEditText] is currently focused.
   */
  private fun TextFieldColors.colorBasedOnFocus(isFocused: Boolean) {
    val container = if (isFocused) focusedContainerColor else unfocusedContainerColor
    val leadingIcon = if (isFocused) focusedLeadingIconColor else unfocusedLeadingIconColor
    val trailingIcon = if (isFocused) focusedTrailingIconColor else unfocusedTrailingIconColor
    val placeholder = if (isFocused) focusedPlaceholderColor else unfocusedPlaceholderColor
    val text = if (isFocused) focusedTextColor else unfocusedTextColor
    color(container, leadingIcon, trailingIcon, placeholder, text)
  }

  /** Applies the error-based [Color]s. */
  private fun TextFieldColors.colorBasedOnError() {
    color(
      errorContainerColor,
      errorLeadingIconColor,
      errorTrailingIconColor,
      errorPlaceholderColor,
      errorTextColor
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      val cursorColorInArgb = cursorColor.toArgb()
      textCursorDrawable?.setTint(cursorColorInArgb)
    }
  }

  /**
   * Applies the [Color]s to their respective targets.
   *
   * @param container [Color] by which the container — the background — will be colored.
   * @param leadingIcon [Color] by which the leading icon — the leading compound [Drawable] — will
   *   be colored.
   * @param trailingIcon [Color] by which the trailing icon — the trailing compound [Drawable] —
   *   will be colored.
   * @param placeholder [Color] by which the placeholder — the hint — will be colored.
   * @param text [Color] by which the text will be colored.
   */
  private fun color(
    container: Color,
    leadingIcon: Color,
    trailingIcon: Color,
    placeholder: Color,
    text: Color
  ) {
    val containerInArgb = container.toArgb()
    val placeholderInArgb = placeholder.toArgb()
    val textInArgb = text.toArgb()
    background?.let { it.color = containerInArgb }
      ?: run { background = ColorDrawable(containerInArgb) }
    compoundDrawables[leadingCompoundDrawableIndex]?.color = leadingIcon.toArgb()
    compoundDrawables[trailingCompoundDrawableIndex]?.color = trailingIcon.toArgb()
    setHintTextColor(placeholderInArgb)
    setTextColor(textInArgb)
  }

  companion object {
    /** Index of the compound [Drawable] at the left of a [InteropEditText]. */
    @VisibleForTesting const val LEFT_COMPOUND_DRAWABLE_INDEX = 0

    /** Index of the compound [Drawable] at the right of a [InteropEditText]. */
    @VisibleForTesting const val RIGHT_COMPOUND_DRAWABLE_INDEX = 2
  }
}
