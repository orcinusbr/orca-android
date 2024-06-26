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

package br.com.orcinus.orca.core.sample.feed.profile.post.content

import br.com.orcinus.orca.core.feed.profile.post.content.Attachment
import br.com.orcinus.orca.ext.uri.URIBuilder

/** [Attachment]s returned by [samples]. */
private val sampleAttachments =
  listOf(
    Attachment(
      description = "A very tall window with lots of windows.",
      URIBuilder.url()
        .scheme("https")
        .host("images.unsplash.com")
        .path("photo-1701432925081-9ccb2455c44c")
        .build()
    ),
    Attachment(
      description = "Brown wooden framed yellow padded chair.",
      URIBuilder.url()
        .scheme("https")
        .host("images.unsplash.com")
        .path("photo-1586023492125-27b2c045efd7")
        .build()
    ),
    Attachment(
      description = "Brown wooden seat beside white wooden table.",
      URIBuilder.url()
        .scheme("https")
        .host("images.unsplash.com")
        .path("photo-1600585152220-90363fe7e115")
        .build()
    ),
    Attachment(
      description =
        "Black flat widescreen computer monitor with Apple Magic Keyboard and Mouse on desk.",
      URIBuilder.url()
        .scheme("https")
        .host("images.unsplash.com")
        .path("photo-1548611716-3000815a5803")
        .build()
    )
  )

/** Sample [Attachment]. */
val Attachment.Companion.sample
  get() = samples.first()

/** Sample [Attachment]s. */
val Attachment.Companion.samples
  get() = sampleAttachments
