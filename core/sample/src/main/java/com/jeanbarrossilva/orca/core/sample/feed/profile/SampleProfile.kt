package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/** [Profile] whose operations are performed in memory and serves as a sample. */
internal interface SampleProfile : Profile {
  /** [SampleTootProvider] by which this [SampleProfile]'s [Toot]s will be provided. */
  val tootProvider: SampleTootProvider

  override suspend fun getToots(page: Int): Flow<List<Toot>> {
    return tootProvider.provideBy(id).filterNotNull().map {
      it.windowed(TOOTS_PER_PAGE, partialWindows = true).getOrElse(page) { emptyList() }
    }
  }

  companion object {
    /** Maximum amount of [Toot]s emitted to [getToots]. */
    const val TOOTS_PER_PAGE = 50
  }
}
