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

package br.com.orcinus.orca.core.sample.feed.profile.type.followable;

import br.com.orcinus.orca.core.feed.profile.post.Author;
import br.com.orcinus.orca.core.feed.profile.type.followable.Follow;
import br.com.orcinus.orca.core.feed.profile.type.followable.FollowableProfile;
import br.com.orcinus.orca.core.sample.InternalSampleApi;
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfile;
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composer;
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers;
import br.com.orcinus.orca.std.markdown.Markdown;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.Nullable;

/**
 * {@link Composer} that is also followable.
 *
 * @param <T> The {@link Follow} status.
 * @see FollowableProfile
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SampleFollowableProfile<T extends Follow> extends FollowableProfile<T>
    implements Composer {
  /** {@link Composer} to which this class' {@link Composer}-specific behavior is delegated. */
  @Delegate @NonNull private final Composer delegateComposer;

  /**
   * Describes who the owner is and/or provides information regarding this {@link
   * FollowableProfile}.
   */
  @Getter @NonNull @With private final Markdown bio;

  /** Current {@link Follow} status. */
  @Getter @NonNull @With private final T follow;

  /** Amount of followers. */
  @Getter private final int followerCount;

  /** Amount of following. */
  @Getter private final int followingCount;

  /**
   * {@link Composer} that is also followable.
   *
   * @param delegate {@link Author} whose equivalent information is to be attributed to this {@link
   *     Composer}.
   * @param bio Describes who the owner is and/or provides information regarding this {@link
   *     FollowableProfile}.
   * @param follow Current {@link Follow} status.
   * @param followerCount Amount of followers.
   * @param followingCount Amount of following.
   * @see FollowableProfile
   */
  @InternalSampleApi
  public SampleFollowableProfile(
      @NonNull Author delegate,
      @NonNull Markdown bio,
      @NonNull T follow,
      int followerCount,
      int followingCount) {
    this.delegateComposer =
        new SampleProfile(delegate) {
          @NonNull
          @Override
          public Markdown getBio() {
            return bio;
          }

          @Override
          public int getFollowerCount() {
            return followerCount;
          }

          @Override
          public int getFollowingCount() {
            return followingCount;
          }
        };
    this.bio = bio;
    this.followerCount = followerCount;
    this.followingCount = followingCount;
    this.follow = follow;
  }

  @Override
  public boolean equals(@Nullable Object other) {
    return other instanceof SampleFollowableProfile<?>
        && Composers.equals(this, (SampleFollowableProfile<?>) other)
        && follow == ((SampleFollowableProfile<?>) other).follow;
  }

  @Override
  public int hashCode() {
    return Composers.hash(this, follow);
  }
}
