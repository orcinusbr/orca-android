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
import br.com.orcinus.orca.core.sample.InternalSampleApi;
import lombok.NonNull;
import lombok.experimental.Delegate;

/**
 * Result of having published a sample {@link Post}, through which other ones can be concatenated
 * and composed.
 */
@InternalSampleApi
public class PublishedComposition {
  /** {@link Composer} by which the {@link Post} has been published. */
  @NonNull private final Composer composer;

  /** {@link Post} that has been published. */
  @NonNull private final Post post;

  /**
   * Composer that composes the {@link Composition} of a sample Post to be published alongside
   * another one.
   */
  private class ConcatenatedComposer implements Composer {
    /** {@link Composer} to which this one's abstractions are delegated. */
    @Delegate @NonNull private final Composer delegate = composer;
  }

  /**
   * Result of having published a sample {@link Post}, through which other ones can be concatenated
   * and composed.
   *
   * @param composer {@link Composer} by which the {@link Post} has been published.
   * @param post {@link Post} that has been published.
   */
  PublishedComposition(@NonNull Composer composer, @NonNull Post post) {
    this.composer = composer;
    this.post = post;
  }

  /** Creates a new {@link Composition} alongside this one. */
  @NonNull
  public Composer and() {
    return new ConcatenatedComposer();
  }

  /** Obtains the {@link Post} that has been published. */
  @NonNull
  public Post get() {
    return post;
  }
}
