package br.com.orcinus.orca.core.feed.profile.type.followable

import assertk.assertThat
import assertk.assertions.isSameAs
import br.com.orcinus.orca.core.sample.feed.profile.type.followable.SampleFollowableProfile
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class FollowServiceTests {
  @Test
  fun toggles() {
    val profileProvider =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val composer = profileProvider.provideCurrent<SampleFollowableProfile<Follow>>()
    lateinit var setFollow: Follow
    val followService =
      object : FollowService() {
        override val profileProvider = profileProvider

        override suspend fun <T : Follow> setFollow(profile: FollowableProfile<T>, follow: T) {
          setFollow = follow
        }
      }
    runTest { followService.toggle(composer.id) }
    assertThat(setFollow).isSameAs(composer.follow.toggled())
  }

  @Test
  fun setsNextStatus() {
    val profileProvider =
      SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
        .withDefaultProfiles()
        .build()
        .profileProvider
    val composer = profileProvider.provideCurrent<SampleFollowableProfile<Follow>>()
    lateinit var setFollow: Follow
    val followService =
      object : FollowService() {
        override val profileProvider = profileProvider

        override suspend fun <T : Follow> setFollow(profile: FollowableProfile<T>, follow: T) {
          setFollow = follow
        }
      }
    runTest { followService.next(composer.id) }
    assertThat(setFollow).isSameAs(composer.follow.next())
  }
}
