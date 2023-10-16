package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.http.instanceProvider
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body

/** [Fetcher] that requests [Toot]s to the API. */
internal object HttpTootFetcher : Fetcher<Toot>() {
  override suspend fun onFetch(key: String): Toot {
    return (Injector.from<HttpModule>().instanceProvider().provide() as SomeHttpInstance)
      .client
      .authenticateAndGet("/api/v1/statuses/$key")
      .body<HttpStatus>()
      .toToot()
  }
}
