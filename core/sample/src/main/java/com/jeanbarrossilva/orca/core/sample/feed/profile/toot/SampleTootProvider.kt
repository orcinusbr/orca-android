package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

/**
 * [TootProvider] that provides sample [Toot]s.
 *
 * @param defaultToots [Toot]s that are present by default.
 */
class SampleTootProvider internal constructor(internal val defaultToots: List<Toot>) :
  TootProvider {
  /** [MutableStateFlow] that provides the [Toot]s. */
  internal val tootsFlow = MutableStateFlow(defaultToots)

  /** [IllegalArgumentException] thrown if a nonexistent [Toot] is requested. */
  class NonexistentTootException internal constructor(id: String) :
    IllegalArgumentException("Toot identified as \"$id\" doesn't exist.")

  override suspend fun provide(id: String): Flow<Toot> {
    return tootsFlow.mapNotNull { toots -> toots.find { toot -> toot.id == id } }
  }

  /**
   * Provides the [Toot]s made by the author whose ID equals to the given one.
   *
   * @param authorID ID of the author whose [Toot]s will be provided.
   */
  internal fun provideBy(authorID: String): Flow<List<Toot>> {
    return tootsFlow.map { toots -> toots.filter { toot -> toot.author.id == authorID } }
  }
}
