package com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache

import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.mastodonte.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache.persistence.MastodonProfileEntityDao
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache.persistence.entity.MastodonProfileEntity
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache.persistence.entity.mapToProfileOptional
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import io.ktor.client.call.body
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import java.util.Optional
import kotlin.time.Duration.Companion.minutes

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
 * @param tootPaginateSource [TootPaginateSource] with which a [MastodonProfileSourceOfTruth] will be
 * created.
 * @param entityDao [MastodonProfileEntityDao] with which a [MastodonProfileSourceOfTruth] will be created.
 **/
fun MastodonProfileStore(
    tootPaginateSource: TootPaginateSource,
    entityDao: MastodonProfileEntityDao
): MastodonProfileStore {
    val fetcher = MastodonProfileFetcher()
    val sourceOfTruth = MastodonProfileSourceOfTruth(tootPaginateSource, entityDao)
    val memoryPolicy = MemoryPolicy
        .MemoryPolicyBuilder<String, Optional<Profile>>()
        .setExpireAfterWrite(2.minutes)
        .build()
    return StoreBuilder.from(fetcher, sourceOfTruth).cachePolicy(memoryPolicy).build()
}

/** [Fetcher] by which the HTTP request to obtain a [Profile] will be performed. **/
private fun MastodonProfileFetcher(): MastodonProfileFetcher {
    return Fetcher.of {
        MastodonHttpClient.authenticateAndGet("/api/v1/accounts/$it").body()
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
        writer = { _, entity -> entity.toProfile(tootPaginateSource) },
        entityDao::delete,
        entityDao::deleteAll
    )
}
