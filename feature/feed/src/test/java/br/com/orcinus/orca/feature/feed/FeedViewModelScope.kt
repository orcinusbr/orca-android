/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.feature.feed

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.loadable.list.ListLoadable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * [CoroutineScope] in which [FeedViewModel] tests can be run.
 *
 * @param delegate [TestScope] for [CoroutineScope]-like behavior.
 * @property viewModel [FeedViewModel] to be tested.
 * @property postID ID of the default [Post] on which operations can be performed.
 * @property postPreviewFlow [Flow] that gets emitted the [PostPreview]s equivalent to the default
 *   [Post].
 */
internal class FeedViewModelScope(
  delegate: TestScope,
  val viewModel: FeedViewModel,
  val postID: String,
  val postPreviewFlow: Flow<PostPreview>
) : CoroutineScope by delegate

/**
 * Runs a [FeedViewModel] test.
 *
 * @param body Lambda in which a [FeedViewModel] is tested.
 */
@OptIn(ExperimentalContracts::class)
internal inline fun runFeedViewModelTest(crossinline body: suspend FeedViewModelScope.() -> Unit) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runTest {
    val application = ApplicationProvider.getApplicationContext<Application>()
    val instance =
      SampleInstance.Builder.create(ComposableImageLoader.Provider.sample)
        .withDefaultProfiles()
        .withDefaultPosts()
        .build()
    val feedProvider = instance.feedProvider
    val postID = feedProvider.provide(page = 0).getValueOrThrow().first().first().id
    val viewModel =
      FeedViewModel(
        application,
        coroutineContext,
        instance.profileSearcher,
        feedProvider,
        instance.postProvider,
        onLinkClick = {},
        Disposition.OnThumbnailClickListener.empty
      )
    val postPreviewFlow =
      viewModel.postPreviewsLoadableFlow
        .filterIsInstance<ListLoadable.Populated<PostPreview>>()
        .map { postPreviewsLoadable ->
          postPreviewsLoadable.content.single { postPreview -> postPreview.id == postID }
        }
    try {
      FeedViewModelScope(this, viewModel, postID, postPreviewFlow).body()
    } finally {
      viewModel.viewModelScope.cancel()
    }
  }
}
