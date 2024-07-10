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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.rem
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.span.isStructurallyEqual
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.span.toSpan
import br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.spanned.span.toStyles
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import br.com.orcinus.orca.std.markdown.style.merge

/** Shorthand for `getMarkdown().getStyles()`. */
private val MarkdownSpanned.styles
  get() = markdown.styles

/**
 * [Markdown]-based [Editable].
 *
 * @param initialMarkdown [Markdown] to be modified.
 * @see markdown
 */
private class MarkdownEditable(override val context: Context, initialMarkdown: Markdown) :
  DefaultEditable(), MarkdownSpannable {
  override val length
    get() = markdown.length

  override var markdown = initialMarkdown

  override fun get(index: Int): Char {
    return markdown[index]
  }

  override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
    return markdown.subSequence(startIndex, endIndex)
  }

  override fun toString(): String {
    return markdown.toString()
  }

  override fun clearSpans() {
    markdown = Markdown.unstyled("$this")
  }

  override fun replace(replacement: CharSequence): Editable {
    markdown = markdown.copy { replacement.toString() }
    return this
  }
}

/**
 * [Markdown]-based [Spannable].
 *
 * @see markdown
 */
internal interface MarkdownSpannable : MarkdownSpanned, Spannable {
  override var markdown: Markdown

  override fun setSpan(what: Any?, start: Int, end: Int, flags: Int) {
    val indices = start until end
    markdown =
      Markdown.styled(
        "$this",
        (what?.toStyles(indices)?.let(styles::plus) ?: (styles % indices)).merge()
      )
  }

  override fun removeSpan(what: Any?) {
    what?.let { _ ->
      getIndexedSpans(context)
        .flatMap(IndexedSpans::toStyles)
        .find { it.toSpan().isStructurallyEqual(context, what) }
        ?.let { markdown = Markdown.styled("$this", styles - it) }
    }
  }
}

/**
 * [Markdown]-based [Spanned].
 *
 * @see markdown
 */
internal interface MarkdownSpanned : Spanned {
  /**
   * [Context] with which [Style]s are converted into spans.
   *
   * @see Style.toSpan
   */
  val context: Context

  /** [Markdown] on which this [Spanned] is based. */
  val markdown: Markdown

  override fun <T : Any?> getSpans(start: Int, end: Int, type: Class<T>?): Array<T> {
    @Suppress("UNCHECKED_CAST")
    return type
      ?.let { _ ->
        styles
          .filter { it.indices.first >= start && it.indices.last < end }
          .map(Style::toSpan)
          .filterIsInstance(type as Class<T & Any>)
          .toTypedArray<Any>()
      }
      .orEmpty() as Array<T>
  }

  override fun getSpanStart(tag: Any?): Int {
    return styles
      .takeIf { tag != null }
      ?.associateWith { it.indices.first }
      ?.filterKeys { it.toSpan().isStructurallyEqual(context, tag!!) }
      ?.values
      ?.firstOrNull()
      ?: -1
  }

  override fun getSpanEnd(tag: Any?): Int {
    return styles
      .takeIf { tag != null }
      ?.associateWith { it.indices.last }
      ?.filterKeys { it.toSpan().isStructurallyEqual(context, tag!!) }
      ?.values
      ?.lastOrNull()
      ?.inc()
      ?: -1
  }

  override fun getSpanFlags(tag: Any?): Int {
    return 0
  }

  override fun nextSpanTransition(start: Int, limit: Int, type: Class<*>?): Int {
    return type?.let { _ ->
      val spans = getSpans(start = start.inc(), end = limit, type)
      if (start <= spans.lastIndex) {
        spans
          .drop(start)
          .withIndex()
          .find { it.index < spans.lastIndex && spans[it.index.inc()]::class != it.value::class }
          ?.index
          ?: limit
      } else {
        limit
      }
    }
      ?: limit
  }
}

/**
 * Converts this [Markdown] into an [Editable].
 *
 * @param context [Context] with which [Style]s are converted into spans.
 * @see Style.toSpan
 */
internal fun Markdown.toEditable(context: Context): Editable {
  return MarkdownEditable(context, this)
}

/**
 * Converts this [Markdown] into a [Spannable].
 *
 * @param context [Context] with which [Style]s are converted into spans.
 * @see Style.toSpan
 */
internal fun Markdown.toSpannable(context: Context): Spannable {
  return object : MarkdownSpannable, CharSequence by this {
    override val context = context
    override var markdown = this@toSpannable

    override fun toString(): String {
      return this@toSpannable.toString()
    }
  }
}

/**
 * Converts this [Markdown] into a [Spanned].
 *
 * @param context [Context] with which [Style]s are converted into spans.
 * @see Style.toSpan
 */
internal fun Markdown.toSpanned(context: Context): Spanned {
  return object : MarkdownSpanned, CharSequence by this {
    override val context = context
    override val markdown = this@toSpanned

    override fun toString(): String {
      return this@toSpanned.toString()
    }
  }
}
