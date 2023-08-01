package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.MemoryPolicy
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.mastodon.Mastodon
import com.jeanbarrossilva.orca.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.account.MastodonAccount
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.persistence.MastodonProfileEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.persistence.entity.MastodonProfileEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.persistence.entity.mapToProfileOptional
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.TootPaginateSource
import io.ktor.client.call.body
import java.util.Optional
import kotlin.time.ExperimentalTime

/** [MastodonProfileStore]'s [Fetcher]. **/
private typealias MastodonProfileFetcher = Fetcher<String, MastodonProfileEntity>

/** [MastodonProfileStore]'s [SourceOfTruth]. **/
private typealias MastodonProfileSourceOfTruth =
    SourceOfTruth<String, MastodonProfileEntity, Optional<Profile>>

/** [Store] for storing [Profile]s. **/
internal typealias MastodonProfileStore = Store<String, Optional<Profile>>

/**
 * [Store] that manages requests to retrieve [Profile]s.
 *
 * @param tootPaginateSource [TootPaginateSource] with which the underlying structures will be
 * created.
 * @param entityDao [MastodonProfileEntityDao] with which a [MastodonProfileSourceOfTruth] will be created.
 **/
@OptIn(ExperimentalTime::class)
fun MastodonProfileStore(
    tootPaginateSource: TootPaginateSource,
    entityDao: MastodonProfileEntityDao
): MastodonProfileStore {
    val fetcher = MastodonProfileFetcher(tootPaginateSource)
    val sourceOfTruth = MastodonProfileSourceOfTruth(tootPaginateSource, entityDao)
    val memoryPolicy = MemoryPolicy
        .MemoryPolicyBuilder<String, Optional<Profile>>()
        .setExpireAfterWrite(Mastodon.cacheExpirationTime)
        .build()
    return StoreBuilder.from(fetcher, sourceOfTruth).cachePolicy(memoryPolicy).build()
}

/** [Fetcher] by which the HTTP request to obtain a [Profile] will be performed. **/
private fun MastodonProfileFetcher(tootPaginateSource: TootPaginateSource): MastodonProfileFetcher {
    return Fetcher.of {
        MastodonHttpClient
            .authenticateAndGet("/api/v1/accounts/$it")
            .body<MastodonAccount>()
            .toProfile(tootPaginateSource)
            .toMastodonProfileEntity()
    }
}

/**
 * [SourceOfTruth] to which fetched [Profile]s will be sent.
 *
 * @param tootPaginateSource [TootPaginateSource] with which each emitted [MastodonProfileEntity]
 * will be converted into a [Profile].
 * @param entityDao [MastodonProfileEntityDao] by which read and write operations will be performed.
 * @see MastodonProfileEntity.toProfile
 **/
private fun MastodonProfileSourceOfTruth(
    tootPaginateSource: TootPaginateSource,
    entityDao: MastodonProfileEntityDao
): MastodonProfileSourceOfTruth {
    return SourceOfTruth.of(
        reader = { entityDao.getByID(it).mapToProfileOptional(tootPaginateSource) },
        writer = { _, entity -> entityDao.insert(entity) },
        entityDao::delete,
        entityDao::deleteAll
    )
}
