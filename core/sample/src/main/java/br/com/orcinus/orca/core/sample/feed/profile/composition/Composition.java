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

package br.com.orcinus.orca.core.sample.feed.profile.composition;

import br.com.orcinus.orca.core.feed.profile.post.Post;
import br.com.orcinus.orca.core.feed.profile.post.content.Content;
import br.com.orcinus.orca.core.sample.InternalSampleApi;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.NonNull;

/**
 * Partial data of a sample {@link Post} whose publication {@link ZonedDateTime} is yet to be
 * defined.
 */
@InternalSampleApi
public class Composition {
  /** {@link Composer} by which the {@link Post} is to be published. */
  @NonNull private final Composer composer;

  /** {@link Content} that has been composed. */
  @NonNull private final Content content;

  /**
   * Partial data of a sample {@link Post} whose publication {@link ZonedDateTime} is yet to be
   * defined.
   *
   * @param composer {@link Composer} by which the {@link Post} is to be published.
   * @param content {@link Content} that has been composed.
   */
  Composition(@NonNull Composer composer, @NonNull Content content) {
    this.composer = composer;
    this.content = content;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Composition
        && composer == ((Composition) other).composer
        && content == ((Composition) other).content;
  }

  @Override
  public int hashCode() {
    return Objects.hash(composer, content);
  }

  /**
   * Specifies the moment in time in which the sample {@link Post} to be published was uploaded.
   *
   * @param publicationDateTime Publication {@link ZonedDateTime} to be set.
   * @see Post#getPublicationDateTime()
   */
  @NonNull
  public TimedComposition on(@NonNull ZonedDateTime publicationDateTime) {
    return new TimedComposition(composer, content, publicationDateTime);
  }
}
