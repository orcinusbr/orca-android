/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.std.styledstring

import com.jeanbarrossilva.orca.std.styledstring.style.Style
import com.jeanbarrossilva.orca.std.styledstring.style.isChoppedBy
import com.jeanbarrossilva.orca.std.styledstring.style.isWithin
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Email
import com.jeanbarrossilva.orca.std.styledstring.style.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention
import java.io.Serializable
import java.net.URL
import java.util.Objects

/**
 * [CharSequence] that supports stylization such as emboldening, italicizing and linking (through
 * plain [URL]s, e-mails, mentions and even simple text).
 *
 * A [StyledString] can be created either through its constructors, or through [buildStyledString].
 *
 * @param text Underlying [String] that's been built.
 * @param styles [Style]s applied to the [text].
 * @see Builder.bold
 * @see Builder.hashtag
 * @see Builder.italic
 * @see Builder.mention
 * @see Builder.build
 */
class StyledString(private val text: String, val styles: List<Style>) :
  CharSequence by text, Serializable {
  /**
   * [StyledString] without [Style]s.
   *
   * @param text [String] without any [Style]s attached to it.
   */
  constructor(text: String) : this(text, styles = emptyList())

  /** Allows text and [Style]s to be appended and for a [StyledString] to be built. */
  class Builder @PublishedApi internal constructor() {
    /** [String] to be the [text][StyledString.text] of the [StyledString] being built. */
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

    /** Appends this [Char] to the [text][StyledString.text] of the [StyledString] being built. */
    operator fun Char.unaryPlus() {
      text += this
    }

    /** Appends this [String] to the [text][StyledString.text] of the [StyledString] being built. */
    operator fun String.unaryPlus() {
      text += this
    }

    /**
     * Emboldens the text to be appended.
     *
     * @param appendix Additionally stylizes the bold text to be appended or solely appends it.
     * @see Bold
     */
    fun bold(appendix: Appender.() -> Unit) {
      append(appendix, ::Bold)
    }

    /**
     * Turns the text to be appended into a hashtag.
     *
     * @param appendix Additionally stylizes the hashtag to be appended or solely appends it.
     * @see Hashtag
     */
    fun hashtag(appendix: Appender.() -> Unit) {
      append(appendix, ::Hashtag)
    }

    /**
     * Italicizes the text to be appended.
     *
     * @param appendix Additionally stylizes the italic text to be appended or solely appends it.
     * @see Italic
     */
    fun italic(appendix: Appender.() -> Unit) {
      append(appendix, ::Italic)
    }

    /**
     * Links the text to be appended to the [url].
     *
     * @param appendix Additionally stylizes the link to be appended or solely appends it.
     * @see Link
     */
    fun link(url: URL, appendix: Appender.() -> Unit) {
      append(appendix) { Link.to(url, it) }
    }

    /**
     * Turns the text to be appended into a mention.
     *
     * @param appendix Additionally stylizes the mention to be appended or solely appends it.
     * @see Mention
     */
    fun mention(url: URL, appendix: Appender.() -> Unit) {
      append(appendix) { Mention(it, url) }
    }

    /**
     * Builds a [StyledString] with the provided [Style]s.
     *
     * @throws Style.Constrained.InvalidTargetException If a constrained [Style] is applied to an
     *   invalid target.
     */
    @PublishedApi
    @Throws(Style.Constrained.InvalidTargetException::class)
    internal fun build(): StyledString {
      ensureStyleConstraints()
      val emails = text.map(Email.regex) { indices, _ -> Email(indices) }
      val plainLinks = text.map(Link.urlRegex) { indices, match -> Link.to(URL(match), indices) }
      val stylesAsList = styles.apply { addAll(emails + plainLinks) }.toList()
      return StyledString(text, stylesAsList)
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

    /**
     * Ensures that the applied constrained [Style]s' targets match their required format.
     *
     * @throws Style.Constrained.InvalidTargetException If a constrained [Style] is applied to an
     *   invalid target.
     * @see Style.Constrained
     */
    @Throws(Style.Constrained.InvalidTargetException::class)
    private fun ensureStyleConstraints() {
      styles
        .filterIsInstance<Style.Constrained>()
        .associateWith { text.substring(it.indices) }
        .entries
        .firstOrNull()
        ?.let { (style, target) ->
          if (!target.matches(style.regex)) {
            throw style.InvalidTargetException(target)
          }
        }
    }
  }

  override fun equals(other: Any?): Boolean {
    return other is String && toString() == other ||
      other is StyledString && text == other.text && styles.containsAll(other.styles)
  }

  override fun hashCode(): Int {
    return Objects.hash(text, styles)
  }

  override fun toString(): String {
    return text
  }

  /**
   * Creates a [StyledString] with this one's [styles] (limited to those within the given [text]'s
   * bounds).
   *
   * @param text Returns the underlying [String] created from the original [StyledString.text] to
   *   which this [StyledString]'s applicable [styles] will be applied.
   */
  fun copy(text: String.() -> String): StyledString {
    val updatedText = toString().text()
    return StyledString(
      updatedText,
      styles
        .filter { it.isWithin(updatedText) || it.isChoppedBy(updatedText) }
        .map({ it.isChoppedBy(updatedText) }) { it.at(it.indices.first..updatedText.lastIndex) }
    )
  }

  companion object
}
