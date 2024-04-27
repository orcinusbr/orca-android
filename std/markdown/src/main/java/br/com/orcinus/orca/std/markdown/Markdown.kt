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

package br.com.orcinus.orca.std.markdown

import br.com.orcinus.orca.std.markdown.style.Style
import br.com.orcinus.orca.std.markdown.style.isChoppedBy
import br.com.orcinus.orca.std.markdown.style.isWithin
import java.io.Serializable
import java.net.URI
import java.util.Objects

/**
 * [CharSequence] that supports stylization such as emboldening, italicizing and linking (through
 * plain [URI]s, e-mails, mentions and even simple text).
 *
 * [Markdown] can be created either through its constructors, or through [buildMarkdown].
 *
 * @param text Underlying [String] that's been built.
 * @param styles [Style]s applied to the [text].
 * @see Builder.bold
 * @see Builder.italic
 * @see Builder.link
 * @see Builder.build
 */
class Markdown(private val text: String, val styles: List<Style>) :
  CharSequence by text, Serializable {
  /**
   * [Markdown] without [Style]s.
   *
   * @param text [String] without any [Style]s attached to it.
   */
  constructor(text: String) : this(text, styles = emptyList())

  /** Allows text and [Style]s to be appended and for [Markdown] to be built. */
  class Builder @PublishedApi internal constructor() {
    /** [String] to be the [text][Markdown.text] of the [Markdown] being built. */
    private var text = ""

    /** [Style]s applied to the [text]. */
    private val styles = mutableListOf<Style>()

    /** [Appender]s that are currently active. */
    private val activeAppenders = mutableListOf<Appender>()

    /**
     * Appends text with the result of [style] applied to it through [append].
     *
     * @param style Creates a [Style] based on the given indices at which it'll be applied.
     */
    inner class Appender internal constructor(private val style: (indices: IntRange) -> Style) {
      /**
       * Appends this [Char], stylizing it with the result of [style] and the [Style]s of
       * [Appender]s that are currently active.
       */
      operator fun Char.unaryPlus() {
        +toString()
      }

      /**
       * Appends this [String], stylizing it with the result of [style] and the [Style]s of
       * [Appender]s that are currently active.
       */
      operator fun String.unaryPlus() {
        val indices = calculateIndicesFor(this)
        val styles = activeAppenders.map { it.style(indices) }
        with(this@Builder) { +this@unaryPlus }
        this@Builder.styles.addAll(styles)
      }

      /** Calculates the indices at which the [text] will be when appended. */
      private fun calculateIndicesFor(text: String): IntRange {
        return this@Builder.text.length..(this@Builder.text.length + text.lastIndex)
      }
    }

    /** Appends this [Char] to the [text][Markdown.text] of the [Markdown] being built. */
    operator fun Char.unaryPlus() {
      text += this
    }

    /** Appends this [String] to the [text][Markdown.text] of the [Markdown] being built. */
    operator fun String.unaryPlus() {
      text += this
    }

    /**
     * Emboldens the text to be appended.
     *
     * @param appendix Additionally stylizes the bold text to be appended or solely appends it.
     * @see Style.Bold
     */
    fun bold(appendix: Appender.() -> Unit) {
      append(appendix, Style::Bold)
    }

    /**
     * Italicizes the text to be appended.
     *
     * @param appendix Additionally stylizes the italic text to be appended or solely appends it.
     * @see Style.Italic
     */
    fun italic(appendix: Appender.() -> Unit) {
      append(appendix, Style::Italic)
    }

    /**
     * Links the text to be appended to the [uri].
     *
     * @param appendix Additionally stylizes the link to be appended or solely appends it.
     * @see Style.Link
     */
    fun link(uri: URI, appendix: Appender.() -> Unit) {
      append(appendix) { Style.Link(uri, it) }
    }

    /** Builds [Markdown] with the provided [Style]s. */
    @PublishedApi
    internal fun build(): Markdown {
      val mailto = { email: String -> URI("mailto", email, null) }
      val emails = text.map(Regex.email) { indices, email -> Style.Link(mailto(email), indices) }
      val urls = text.map(Regex.url) { indices, url -> Style.Link(URI(url), indices) }
      return Markdown(text, styles + emails + urls)
    }

    /**
     * Creates an [Appender] and runs [appendix] on it, making it active for as long as it's being
     * used.
     *
     * @param appendix Additionally stylizes the text to be appended or solely appends it.
     * @param style Creates the [Style] to be applied to the appended text at the specified indices.
     */
    private fun append(appendix: Appender.() -> Unit, style: (IntRange) -> Style) {
      val appender = Appender(style)
      activeAppenders.add(appender)
      appender.appendix()
      activeAppenders.remove(appender)
    }
  }

  override fun equals(other: Any?): Boolean {
    return other is String && toString() == other ||
      other is Markdown && text == other.text && styles.containsAll(other.styles)
  }

  override fun hashCode(): Int {
    return Objects.hash(text, styles)
  }

  override fun toString(): String {
    return text
  }

  /**
   * Creates [Markdown] with this one's [styles] (limited to those within the given [text]'s
   * bounds).
   *
   * @param text Returns the underlying [String] created from the original [Markdown.text] to which
   *   this [Markdown]'s applicable [styles] will be applied.
   */
  fun copy(text: String.() -> String): Markdown {
    val updatedText = toString().text()
    return Markdown(
      updatedText,
      styles
        .filter { it.isWithin(updatedText) || it.isChoppedBy(updatedText) }
        .map({ it.isChoppedBy(updatedText) }) { it.at(it.indices.first..updatedText.lastIndex) }
    )
  }

  companion object
}
