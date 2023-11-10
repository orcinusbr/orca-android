package com.jeanbarrossilva.orca.std.styledstring

import com.jeanbarrossilva.orca.std.styledstring.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.type.Email
import com.jeanbarrossilva.orca.std.styledstring.type.Hashtag
import com.jeanbarrossilva.orca.std.styledstring.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.type.Link
import com.jeanbarrossilva.orca.std.styledstring.type.Mention
import java.net.URL

/**
 * Converts this [String] into a [StyledString].
 *
 * @param boldDelimiter [Bold.Delimiter] by which this [String]'s emboldened portions are delimited.
 * @param emailDelimiter [Email.Delimiter] by which this [String]'s e-mails are delimited.
 * @param hashtagDelimiter [Hashtag.Delimiter] by which this [String]'s hashtags are delimited.
 * @param italicDelimiter [Italic.Delimiter] by which this [String]'s italicized portions are
 *   delimited.
 * @param linkDelimiter [Link.Delimiter] by which this [String]'s links are delimited.
 * @param mentionDelimiter [Mention.Delimiter] by which this [String]'s mentions are delimited.
 * @param mentioning Given its start index, maps each mention to a username to a [URL].
 * @see Bold
 * @see Email
 * @see Hashtag
 * @see Italic
 * @see Link
 * @see Mention
 */
fun String.toStyledString(
  boldDelimiter: Bold.Delimiter? = Bold.Delimiter.Default,
  emailDelimiter: Email.Delimiter = Email.Delimiter.Default,
  hashtagDelimiter: Hashtag.Delimiter? = Hashtag.Delimiter.Default,
  italicDelimiter: Italic.Delimiter? = Italic.Delimiter.Default,
  linkDelimiter: Link.Delimiter? = Link.Delimiter.Plain,
  mentionDelimiter: Mention.Delimiter? = Mention.Delimiter.Default,
  mentioning: (startIndex: Int) -> URL? = { null }
): StyledString {
  val text =
    StyledString.normalize(
      this,
      boldDelimiter,
      emailDelimiter,
      hashtagDelimiter,
      italicDelimiter,
      linkDelimiter,
      mentionDelimiter
    )
  val emboldened = text.stylize(Bold.Delimiter.Default) { indices, _ -> Bold(indices) }
  val emails = text.stylize(Email.Delimiter.Default) { indices, _ -> Email(indices) }
  val hashtags = text.stylize(Hashtag.Delimiter.Default) { indices, _ -> Hashtag(indices) }
  val italicized = text.stylize(Italic.Delimiter.Default) { indices, _ -> Italic(indices) }
  val links =
    text.stylize(Link.Delimiter.Plain) { indices, target -> Link.to(URL(target), indices) }
  val mentions =
    text.stylize(Mention.Delimiter.Default) { indices, _ ->
      mentioning(indices.first)?.let { target -> Mention(indices, target) }
    }
  return StyledString(text, emails + emboldened + hashtags + italicized + links + mentions)
}

/**
 * Gets the [Style]s present in this [String] according to the [delimiter].
 *
 * @param delimiter [Style.Delimiter] by which the [Style]s to be obtained are delimited.
 * @param style Returns the respective [Style] applied to the given [indices] of this [String].
 */
internal fun <T : Style> String.stylize(
  delimiter: Style.Delimiter?,
  style: (indices: IntRange, target: String) -> T?
): List<T> {
  return delimiter?.delimit(this)?.mapNotNull { style(it.range, it.value) }.orEmpty()
}
