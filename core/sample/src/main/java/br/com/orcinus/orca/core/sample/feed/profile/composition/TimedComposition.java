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

import br.com.orcinus.orca.core.feed.profile.post.Author;
import br.com.orcinus.orca.core.feed.profile.post.Post;
import br.com.orcinus.orca.core.feed.profile.post.content.Content;
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost;
import br.com.orcinus.orca.core.feed.profile.post.repost.Reposts;
import br.com.orcinus.orca.core.sample.InternalSampleApi;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt;
import lombok.NonNull;

/** Composition to which a publication {@link ZonedDateTime} has been provided. */
@InternalSampleApi
public class TimedComposition {
  /** {@link Composer} by which the {@link Post} is composed. */
  @NonNull private final Composer composer;

  /**
   * {@link Lazy} containing the {@link TimedComposition#composer} converted into an {@link Author}.
   */
  @NonNull private final Lazy<Author> composerAuthorLazy;

  /** {@link Content} that has been composed. */
  @NonNull private final Content content;

  /** Moment in which the sample {@link Post} is considered to have been published. */
  @NonNull private final ZonedDateTime publicationDateTime;

  /** Determines how a {@link Post} should be published. */
  public abstract static class PublishingStrategy {
    /** Strategy returned by {@link PublishingStrategy#asOwned()}. */
    private static final PublishingStrategy owned =
        new PublishingStrategy() {
          @NonNull
          @Override
          Post createPost(TimedComposition timedComposition) {
            final Author author = timedComposition.composerAuthorLazy.getValue();
            return timedComposition.toPost(author);
          }
        };

    /** Determines how a {@link Post} should be published. */
    private PublishingStrategy() {}

    /**
     * Strategy for publishing a {@link Post} as it having been authored by the {@link Composer}.
     */
    @NonNull
    public static PublishingStrategy asOwned() {
      return owned;
    }

    /**
     * Strategy for publishing a {@link Post} as it being a {@link Repost} of one authored by
     * someone else.
     *
     * @param author {@link Author} of the original {@link Post} of which a {@link Repost} is to be
     *     published.
     */
    @NonNull
    public static PublishingStrategy asRepostFrom(Author author) {
      return new PublishingStrategy() {
        @Override
        @NonNull
        Repost createPost(TimedComposition timedComposition) {
          final Post post = timedComposition.toPost(author);
          final Author reposter = timedComposition.composerAuthorLazy.getValue();
          return Reposts.create(post, reposter);
        }
      };
    }

    /**
     * Creates a {@link Post} from the given {@link TimedComposition} according to the intent of
     * this strategy.
     *
     * @param timedComposition {@link TimedComposition} from which a {@link Post} is to be created.
     */
    @NonNull
    abstract Post createPost(TimedComposition timedComposition);
  }

  /**
   * Composition to which a publication {@link ZonedDateTime} has been provided.
   *
   * @param composer {@link Composer} by which the {@link Post} is composed.
   * @param content {@link Content} that has been composed.
   * @param publicationDateTime Moment in which the sample {@link Post} is considered to have been
   *     published.
   */
  TimedComposition(
      @NonNull Composer composer,
      @NonNull Content content,
      @NonNull ZonedDateTime publicationDateTime) {
    this.composer = composer;
    this.composerAuthorLazy = LazyKt.lazy(() -> Composers.toAuthor(composer));
    this.content = content;
    this.publicationDateTime = publicationDateTime;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof TimedComposition
        && composer == ((TimedComposition) other).composer
        && content == ((TimedComposition) other).content
        && publicationDateTime == ((TimedComposition) other).publicationDateTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(composer, content, publicationDateTime);
  }

  /**
   * Publishes the sample {@link Post}.
   *
   * @param strategy Determines how the {@link Post} should be published — either as a regular,
   *     independent one composed by the {@link TimedComposition#composer} themselves or a {@link
   *     Repost} of someone else's.
   * @see PublishingStrategy#asOwned()
   * @see PublishingStrategy#asRepostFrom(Author)
   */
  public PublishedComposition publish(@NonNull PublishingStrategy strategy) {
    final Post post = strategy.createPost(this);
    publish(post);
    return new PublishedComposition(composer, post);
  }

  /**
   * Converts this {@link TimedComposition} into a {@link Post}.
   *
   * @param author {@link Author} that has authored the {@link Post}.
   */
  private Post toPost(Author author) {
    return composer.createPost(author, content, publicationDateTime);
  }

  /**
   * Adds the given {@link Post} to the {@link TimedComposition#composer}.
   *
   * @param post {@link Post} to be published — that is, added.
   */
  private void publish(@NonNull Post post) {
    final List<Post> currentPosts = Composers.getPosts(composer);
    final int updatedPostsCount = currentPosts.size() + 1;
    final ArrayList<Post> updatedPosts = new ArrayList<>(updatedPostsCount);
    updatedPosts.addAll(currentPosts);
    updatedPosts.add(post);
    composer.getPostsFlow().setValue(updatedPosts);
  }
}
