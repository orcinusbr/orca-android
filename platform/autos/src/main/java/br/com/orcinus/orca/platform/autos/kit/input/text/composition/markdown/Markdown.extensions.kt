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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.markdown

import android.content.Context
import android.text.Editable
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated.DefaultEditable
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated.span.toStyles
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.IndexedSpans
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.getIndexedSpans
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.span.isStructurallyEqual
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.span.toSpan
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking

/**
 * [Markdown]-based [Editable].
 *
 * ###### API notes
 *
 * [Markdown] objects are immutable by nature, and that is why an [Editable] of one requires
 * multiples instances of it to be created and have these creations be observed by a consumer in
 * order to provide some sort of functionality that relies on mutability.
 *
 * Each mutation-inducing method ([setSpan], [removeSpan], [replace], [append], [clear], [delete],
 * [insert] and [clearSpans]) causes an emission to be sent to a [Flow]; but, because modifications
 * to a [MarkdownEditable] are emitted blockingly by the [flowCollector], an instance of it must
 * instantly be sent to the [Flow] returned by [toEditableAsFlow] (given that this method calls
 * itself upon these alterations); otherwise, the [Flow] would never get emitted to.
 *
 * @param context [Context] with which [Style] are converted into spans.
 * @param flowCollector [FlowCollector] that emits changes (such as setting and removing spans).
 * @param markdown [Markdown] based on which this [Editable] is.
 * @see Style.toSpan
 */
private class MarkdownEditable(
  private val context: Context,
  private val flowCollector: FlowCollector<Editable>,
  private val markdown: Markdown
) : DefaultEditable() {
  override val length
    get() = markdown.length

  override fun get(index: Int): Char {
    return markdown[index]
  }

  override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
    return markdown.subSequence(startIndex, endIndex)
  }

  override fun setSpan(what: Any?, start: Int, end: Int, flags: Int) {
    what?.toStyles(start..end)?.let {
      runBlocking { flowCollector.emitAll(Markdown.styled("$this", it).toEditableAsFlow(context)) }
    }
  }

  override fun removeSpan(what: Any?) {
    what?.let {
      runBlocking {
        flowCollector.emitAll(
          Markdown.styled("$this", getIndexedSpans(context).flatMap(IndexedSpans::toStyles))
            .toEditableAsFlow(context)
        )
      }
    }
  }

  override fun clearSpans() {
    runBlocking { flowCollector.emitAll(Markdown.unstyled("$this").toEditableAsFlow(context)) }
  }

  override fun replace(replacement: CharSequence): Editable {
    return runBlocking {
      markdown.copy { replacement.toString() }.toEditableAsFlow(context).single()
    }
  }

  override fun <T : Any?> getSpans(start: Int, end: Int, type: Class<T>?): Array<T> {
    @Suppress("UNCHECKED_CAST")
    return type
      ?.let { _ ->
        markdown.styles
          .filter { it.indices.first >= start && it.indices.last < end }
          .map(Style::toSpan)
          .filterIsInstance(type as Class<T & Any>)
          .toTypedArray<Any>()
      }
      .orEmpty() as Array<T>
  }

  override fun getSpanStart(tag: Any?): Int {
    return markdown.styles
      .takeIf { tag != null }
      ?.associateWith { it.indices.first }
      ?.filterKeys { it.toSpan().isStructurallyEqual(context, tag!!) }
      ?.values
      ?.lastOrNull()
      ?: -1
  }

  override fun getSpanEnd(tag: Any?): Int {
    return markdown.styles
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
 * Converts this [Markdown] into a [Flow] of [Editable].
 *
 * @param context [Context] with which [Style]s are converted into spans.
 * @see Style.toSpan
 */
internal fun Markdown.toEditableAsFlow(context: Context): Flow<Editable> {
  return flow { emit(MarkdownEditable(context, this, this@toEditableAsFlow)) }
}
