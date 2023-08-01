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

/** [ProfileSearchResultsStore]'s [Fetcher]. **/
private typealias ProfileSearchResultsFetcher =
    Fetcher<String, List<ProfileSearchResultEntity>>

/** [ProfileSearchResultsStore]'s [SourceOfTruth]. **/
private typealias ProfileSearchResultsSourceOfTruth =
    SourceOfTruth<String, List<ProfileSearchResultEntity>, List<ProfileSearchResult>>

/** [Store] for [ProfileSearchResult]s. **/
typealias ProfileSearchResultsStore = Store<String, List<ProfileSearchResult>>

/**
 * [Store] that manages requests to retrieve [ProfileSearchResult]s.
 *
 * @param tootPaginateSource [TootPaginateSource] with which the underlying structures will be
 * created.
 * @param entityDao [ProfileSearchResultEntityDao] with which a
 * [ProfileSearchResultsSourceOfTruth] will be created.
 **/
@OptIn(ExperimentalTime::class)
fun ProfileSearchResultsStore(
    tootPaginateSource: TootPaginateSource,
    entityDao: ProfileSearchResultEntityDao
): ProfileSearchResultsStore {
    val fetcher = ProfileSearchResultsFetcher(tootPaginateSource)
    val sourceOfTruth = ProfileSearchResultsSourceOfTruth(entityDao)
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
private fun ProfileSearchResultsFetcher(tootPaginateSource: TootPaginateSource):
    ProfileSearchResultsFetcher {
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
private fun ProfileSearchResultsSourceOfTruth(entityDao: ProfileSearchResultEntityDao):
    ProfileSearchResultsSourceOfTruth {
    return SourceOfTruth.of(
        reader = { entityDao.selectByQuery(it).mapToProfileSearchResults() },
        writer = { _, entities -> entityDao.insert(entities) },
        entityDao::delete,
        entityDao::deleteAll
    )
}
