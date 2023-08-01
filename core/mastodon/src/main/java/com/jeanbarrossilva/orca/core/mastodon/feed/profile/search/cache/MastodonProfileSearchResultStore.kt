package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.MemoryPolicy
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.toProfileSearchResult
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.persistence.ProfileSearchResultEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.persistence.entity.ProfileSearchResultEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.persistence.entity.mapToProfileSearchResults
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.persistence.entity.toProfileSearchResultEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import kotlin.time.ExperimentalTime

/** [MastodonProfileSearchResultsStore]'s [Fetcher]. **/
private typealias MastodonProfileSearchResultsFetcher =
    Fetcher<String, List<ProfileSearchResultEntity>>

/** [MastodonProfileSearchResultsStore]'s [SourceOfTruth]. **/
private typealias MastodonProfileSearchResultsSourceOfTruth =
    SourceOfTruth<String, List<ProfileSearchResultEntity>, List<ProfileSearchResult>>

/** [Store] for [ProfileSearchResult]s. **/
typealias MastodonProfileSearchResultsStore = Store<String, List<ProfileSearchResult>>

/**
 * [Store] that manages requests to retrieve [ProfileSearchResult]s.
 *
 * @param tootPaginateSource [TootPaginateSource] with which the underlying structures will be
  created.
 * @param entityDao [ProfileSearchResultEntityDao] with which a
 * [MastodonProfileSearchResultsSourceOfTruth] will be created.
 **/
@OptIn(ExperimentalTime::class)
fun MastodonProfileSearchResultsStore(
    tootPaginateSource: TootPaginateSource,
    entityDao: ProfileSearchResultEntityDao
): MastodonProfileSearchResultsStore {
    val fetcher = MastodonProfileSearchResultsFetcher(tootPaginateSource)
    val sourceOfTruth = MastodonProfileSearchResultsSourceOfTruth(entityDao)
    val memoryPolicy = MemoryPolicy
        .MemoryPolicyBuilder<String, List<ProfileSearchResult>>()
        .setExpireAfterWrite(Mastodon.cacheExpirationTime)
        .build()
    return StoreBuilder.from(fetcher, sourceOfTruth).cachePolicy(memoryPolicy).build()
}

/**
 * [Fetcher] by which the HTTP request to obtain [ProfileSearchResult]s will be performed.
 *
 * @param tootPaginateSource [TootPaginateSource] with which each emitted [List] of
 * profile search result entities will be converted into one of [ProfileSearchResult]s.
 *
 * @see ProfileSearchResultEntity
 **/
fun MastodonProfileSearchResultsFetcher(tootPaginateSource: TootPaginateSource):
    MastodonProfileSearchResultsFetcher {
    return Fetcher.of { query ->
        MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/search") { parameter("q", query) }
            .body<List<MastodonAccount>>()
            .map { account ->
                account
                    .toProfile(tootPaginateSource)
                    .toProfileSearchResult()
                    .toProfileSearchResultEntity(query)
            }
    }
}

/**
 * [SourceOfTruth] to which fetched [ProfileSearchResult]s will be sent.
 *
 * @param entityDao [ProfileSearchResultEntityDao] by which read and write operations will be
 * performed.
 * @see ProfileSearchResultEntity.toProfileSearchResult
 **/
private fun MastodonProfileSearchResultsSourceOfTruth(entityDao: ProfileSearchResultEntityDao):
    MastodonProfileSearchResultsSourceOfTruth {
    return SourceOfTruth.of(
        reader = { entityDao.selectByQuery(it).mapToProfileSearchResults() },
        writer = { _, entities -> entityDao.insert(entities) },
        entityDao::delete,
        entityDao::deleteAll
    )
}
