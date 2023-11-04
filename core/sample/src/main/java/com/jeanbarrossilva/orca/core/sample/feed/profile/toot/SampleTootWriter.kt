package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import kotlinx.coroutines.flow.update

/**
 * Performs [Toot]-related writing operations.
 *
 * @param tootProvider [SampleTootProvider] by which [Toot]s will be provided.
 */
class SampleTootWriter internal constructor(private val tootProvider: SampleTootProvider) {
  /** Clears all added [Toot]s, including the default ones. */
  fun clear() {
    tootProvider.tootsFlow.update { emptyList() }
  }

  /** Resets this [SampleTootWriter] to its default state. */
  fun reset() {
    tootProvider.tootsFlow.value = tootProvider.defaultToots
  }
}
