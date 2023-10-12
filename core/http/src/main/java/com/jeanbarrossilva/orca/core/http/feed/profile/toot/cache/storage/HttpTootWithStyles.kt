package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntity

/**
 * Relationship between the [toot] and the [styles] applied to it.
 *
 * @param toot [HttpTootEntity] to which the [styles] are applied.
 * @param styles [HTTP style entities][HttpStyleEntity] that belong to the [toot].
 */
internal data class HttpTootWithStyles(
  @Embedded val toot: HttpTootEntity,
  @Relation(parentColumn = "id", entityColumn = "toot_id") val styles: List<HttpStyleEntity>
)
