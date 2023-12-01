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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.account.at
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.account.sample
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import java.util.UUID

/** ID of an [Author] created by [createRamboSample]. */
private val ramboSampleAuthorID = UUID.randomUUID().toString()

/**
 * Creates a sample [Author].
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Author]'s avatar will be loaded from a [SampleImageSource].
 */
fun Author.Companion.createSample(
  avatarLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Author {
  return Author(
    Actor.Authenticated.sample.id,
    avatarLoaderProvider.provide(AuthorImageSource.Default),
    name = "Jean Silva",
    Account.sample,
    profileURL = URL("https://mastodon.social/@jeanbarrossilva")
  )
}

/**
 * Creates a sample author based on [@_inside@mastodon.social](https://mastodon.social/@_inside).
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Author]'s avatar will be loaded from a [SampleImageSource].
 */
fun Author.Companion.createRamboSample(
  avatarLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Author {
  return Author(
    ramboSampleAuthorID,
    avatarLoaderProvider.provide(AuthorImageSource.Rambo),
    name = "Guilherme Rambo",
    account = "_inside" at "mastodon.social",
    profileURL = URL("https://mastodon.social/@_inside")
  )
}
