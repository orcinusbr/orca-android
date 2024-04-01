/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.post.figure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.GalleryPreview
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.thumbnail.Thumbnail
import com.jeanbarrossilva.orca.composite.timeline.post.figure.link.LinkCard
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import java.net.URL

/** Interactive portion of a [PostPreview]. */
@Immutable
sealed class Figure {
  /**
   * [Figure] that displays the data provided by the [headline].
   *
   * @param headline [Headline] with the main information about a mentioned link.
   * @param onClick Lambda to be run whenever the [LinkCard] is clicked.
   */
  internal data class Link(private val headline: Headline, private val onClick: () -> Unit) :
    Figure() {
    @Composable
    override fun Content(modifier: Modifier) {
      LinkCard(headline, onClick, modifier)
    }
  }

  /**
   * [Figure] that displays the [Thumbnail]s of the [preview].
   *
   * @param preview [GalleryPreview] with the information to be previewed.
   * @param onThumbnailClickListener [Disposition.OnThumbnailClickListener] that is notified of
   *   clicks on [Thumbnail]s.
   */
  internal data class Gallery
  @Throws(IllegalArgumentException::class)
  constructor(
    private val preview: GalleryPreview,
    private val onThumbnailClickListener: Disposition.OnThumbnailClickListener =
      Disposition.OnThumbnailClickListener.empty
  ) : Figure() {
    init {
      require(preview.attachments.isNotEmpty()) { "Cannot create a gallery without attachments." }
    }

    @Composable
    override fun Content(modifier: Modifier) {
      GalleryPreview(preview, onThumbnailClickListener, modifier)
    }
  }

  /**
   * UI content to be displayed by this [Figure].
   *
   * @param modifier [Modifier] to be applied to the underlying [Composable].
   */
  @Composable abstract fun Content(modifier: Modifier)

  /** UI content to be displayed by this [Figure]. */
  @Composable
  fun Content() {
    Content(Modifier)
  }

  companion object {
    /**
     * Creates the appropriate [Figure] for the [content].
     *
     * @param postID ID of the [Post] to which the [content] belongs.
     * @param authorName Name of the [Author] by which the [Post] has been published.
     * @param content [Content] from which the [Figure] will be created.
     * @param onLinkClick Lambda to be run whenever a [LinkCard] is clicked.
     * @param onThumbnailClickListener [Disposition.OnThumbnailClickListener] that is notified of
     *   clicks on [Thumbnail]s.
     * @return Either a [Gallery] if the [content] has [Attachment]s or a [Link] if it has a
     *   [Highlight].
     */
    fun of(
      postID: String,
      authorName: String,
      content: Content,
      onLinkClick: (URL) -> Unit = {},
      onThumbnailClickListener: Disposition.OnThumbnailClickListener =
        Disposition.OnThumbnailClickListener.empty
    ): Figure? {
      return when {
        content.attachments.isNotEmpty() ->
          Gallery(GalleryPreview(postID, authorName, content.attachments), onThumbnailClickListener)
        content.highlight != null ->
          with(content.highlight!!) { Link(headline) { onLinkClick(url) } }
        else -> null
      }
    }
  }
}
