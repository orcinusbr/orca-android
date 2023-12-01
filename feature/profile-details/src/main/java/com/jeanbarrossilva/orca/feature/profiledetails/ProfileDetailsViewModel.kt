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

package com.jeanbarrossilva.orca.feature.profiledetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeanbarrossilva.loadable.flow.loadable
import com.jeanbarrossilva.loadable.list.flow.listLoadable
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.toPostPreviewFlow
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.platform.ui.core.context.share
import com.jeanbarrossilva.orca.platform.ui.core.flatMapEach
import java.net.URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class ProfileDetailsViewModel
private constructor(
  private val contextProvider: ContextProvider,
  private val profileProvider: ProfileProvider,
  private val postProvider: PostProvider,
  coroutineDispatcher: CoroutineDispatcher,
  private val id: String
) : ViewModel() {
  private val coroutineScope = viewModelScope + coroutineDispatcher
  private val profileFlow = MutableSharedFlow<Profile>()
  private val postsIndexFlow = MutableStateFlow(0)

  private val colors
    get() = AutosTheme.getColors(context)

  private val context
    get() = contextProvider.provide()

  val detailsLoadableFlow =
    profileFlow.map { it.toProfileDetails(coroutineScope, colors) }.loadable(coroutineScope)

  @OptIn(ExperimentalCoroutinesApi::class)
  val postPreviewsLoadableFlow =
    postsIndexFlow
      .flatMapConcat(::getPostPreviewsAt)
      .listLoadable(coroutineScope, SharingStarted.WhileSubscribed())

  init {
    requestRefresh()
  }

  fun requestRefresh(onRefresh: () -> Unit = {}) {
    viewModelScope.launch {
      profileFlow.emitAll(provideProfileFlow())
      onRefresh()
    }
  }

  fun share(url: URL) {
    context.share("$url")
  }

  fun favorite(postID: String) {
    coroutineScope.launch { postProvider.provide(postID).first().favorite.toggle() }
  }

  fun repost(postID: String) {
    coroutineScope.launch { postProvider.provide(postID).first().repost.toggle() }
  }

  fun loadPostsAt(index: Int) {
    postsIndexFlow.value = index
  }

  private suspend fun provideProfileFlow(): Flow<Profile> {
    return profileProvider.provide(id).filterNotNull()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  private fun getPostPreviewsAt(index: Int): Flow<List<PostPreview>> {
    return profileFlow.filterNotNull().flatMapConcat { profile ->
      profile.getPosts(index).flatMapEach { post -> post.toPostPreviewFlow(colors) }
    }
  }

  companion object {
    fun createFactory(
      contextProvider: ContextProvider,
      profileProvider: ProfileProvider,
      postProvider: PostProvider,
      id: String
    ): ViewModelProvider.Factory {
      return viewModelFactory {
        addInitializer(ProfileDetailsViewModel::class) {
          ProfileDetailsViewModel(
            contextProvider,
            profileProvider,
            postProvider,
            Dispatchers.Main.immediate,
            id
          )
        }
      }
    }
  }
}
