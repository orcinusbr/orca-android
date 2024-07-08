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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.setPadding
import br.com.orcinus.orca.autos.Spacings
import br.com.orcinus.orca.platform.autos.InternalPlatformAutosApi
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.CompositionTextFieldValue
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.orEmpty
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.toEditableAsFlow
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.toMarkdown
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.toMarkdown
import br.com.orcinus.orca.std.markdown.Markdown
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Text field for composing or editing a post.
 *
 * @param context [Context] in which it will be added.
 * @param attributeSet Attributes specified in XML.
 * @param defaultStyleAttribute Attribute of the style to be applied by default.
 */
class CompositionTextField
@JvmOverloads
constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  defaultStyleAttribute: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attributeSet, defaultStyleAttribute) {
  /** [CompositionTextFieldValue] holding both the current text and selection. */
  private var value = CompositionTextFieldValue.Empty

  /** [OnValueChangeListener] that is notified whenever text-related changes are performed. */
  private var onValueChangeListener: OnValueChangeListener? = null

  /** Delegate by which the [error] is measured and drawn. */
  private val errorDelegate by lazy { ErrorDelegate(this) }

  /**
   * [CoroutineScope] in which [setText]'s [Job]s are launched for defining [Markdown] as being the
   * current text.
   */
  private var markdownTextSettingCoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

  /** [CancellationException] thrown when a [CompositionTextField]'s text is reset. */
  class ResetTextException internal constructor() :
    CancellationException("The text has been reset.")

  /**
   * [CancellationException] thrown when a [CompositionTextField] gets detached from the [Window].
   *
   * @see onDetachedFromWindow
   */
  class DetachedFromWindowException internal constructor() :
    CancellationException("The text field got detached from the window.")

  /**
   * Text field for composing or editing a post.
   *
   * @param context [Context] in which it will be added.
   * @param markdownTextSettingCoroutineScope [CoroutineScope] in which [setText]'s [Job]s are
   *   launched for defining [Markdown] as being the current text.
   */
  @InternalPlatformAutosApi
  constructor(context: Context, markdownTextSettingCoroutineScope: CoroutineScope) : this(context) {
    this.markdownTextSettingCoroutineScope = markdownTextSettingCoroutineScope
  }

  init {
    setBackgroundColor(getBackgroundColor(context))
    setPadding(getSpacing(context))
    compoundDrawablePadding = getSpacing(context)
    gravity = Gravity.TOP
    setTextAppearance(context, R.style.Theme_Orca_Typography_BodyMedium)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    errorDelegate.draw(canvas)
  }

  override fun setText(text: CharSequence?, type: BufferType?) {
    super.setText(text, type)

    @Suppress("UNNECESSARY_SAFE_CALL")
    markdownTextSettingCoroutineScope
      ?.coroutineContext
      ?.get(Job)
      ?.cancelChildren(ResetTextException())

    onValueChangeListener?.let {
      value = text?.toMarkdown(context)?.let(::CompositionTextFieldValue).orEmpty()
      it.onValueChange(value)
    }
  }

  override fun onSelectionChanged(selStart: Int, selEnd: Int) {
    super.onSelectionChanged(selStart, selEnd)

    onValueChangeListener?.let { onValueChangeListener ->
      value =
        text
          ?.toMarkdown(context)
          ?.let { textAsMarkdown -> CompositionTextFieldValue(textAsMarkdown, selStart..selEnd) }
          .orEmpty()
      onValueChangeListener.onValueChange(value)
    }
  }

  override fun getError(): CharSequence? {
    return errorDelegate.error
  }

  override fun setError(error: CharSequence?, icon: Drawable?) {
    errorDelegate.toggle(error)
  }

  override fun onConfigurationChanged(newConfig: Configuration?) {
    errorDelegate.invalidate(newConfig)
    super.onConfigurationChanged(newConfig)
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    markdownTextSettingCoroutineScope.coroutineContext[Job]?.cancelChildren(
      DetachedFromWindowException()
    )
  }

  /**
   * Changes both the text and the selection.
   *
   * @param value [CompositionTextFieldValue] based on which the text and the selection will be set.
   */
  fun setValue(value: CompositionTextFieldValue) {
    if (value != this.value) {
      setText(value.text)
      setSelection(value.selection)
    }
  }

  /**
   * Sets the [Markdown] as the current text.
   *
   * @param text [Markdown] to be set as the text.
   */
  fun setText(text: Markdown) {
    val textAsEditableFlow = text.toEditableAsFlow(context)
    markdownTextSettingCoroutineScope.coroutineContext[Job]?.cancelChildren()

    /*
     * Because the first editable is emitted immediately, the thread isn't actually blocked.
     * runBlocking is called for the initial synchronicity to be maintained, given that changes to
     * the emitted editable are still collected in the background and applied to this text field.
     *
     * For a detailed explanation on the reasoning behind such behavior, refer to MarkdownEditable's
     * documentation (more specifically, the section of notes on its API).
     */
    runBlocking { this@CompositionTextField.text = textAsEditableFlow.first() }

    markdownTextSettingCoroutineScope.launch {
      textAsEditableFlow.collect { this@CompositionTextField.text = it }
    }
  }

  /**
   * Sets an [OnValueChangeListener].
   *
   * @param onValueChangeListener [OnValueChangeListener] that will be notified when the [value]
   *   changes.
   */
  fun setOnValueChangeListener(onValueChangeListener: OnValueChangeListener?) {
    this.onValueChangeListener = onValueChangeListener
  }

  /**
   * Defines the given indices as the ones currently selected.
   *
   * @param selection Portion of the text to be selected.
   */
  private fun setSelection(selection: IntRange) {
    if (selection.isEmpty()) {
      setSelection(0)
    } else {
      setSelection(selection.first, selection.last)
    }
  }

  companion object {
    /**
     * Obtains the amount of pixels by which the surroundings are to be padded and drawn components
     * are to be spaced.
     *
     * @param context [Context] with which conversion from DPs to pixels is performed.
     */
    @JvmName("getSpacing")
    @JvmStatic
    @Px
    fun getSpacing(context: Context): Int {
      return Units.dp(context, Spacings.default.medium)
    }

    /**
     * Obtains the color of a [CompositionTextField]'s background.
     *
     * @param context [Context] with which the color will be resolved from its resource.
     */
    @ColorInt
    @JvmName("getBackgroundColor")
    @JvmStatic
    internal fun getBackgroundColor(context: Context): Int {
      return context.getColor(R.color.surfaceContainer)
    }
  }
}
