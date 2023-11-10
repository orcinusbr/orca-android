package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage

import androidx.room.Embedded
import androidx.room.Relation
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.style.HttpStyleEntity

/**
 * Relationship between the [profile] and the [styles] applied to its [bio][HttpProfileEntity.bio].
 *
 * @param profile [HttpProfileEntity] whose [bio][HttpProfileEntity.bio] has the [styles] are
 *   applied.
 * @param styles [HTTP style entities][HttpStyleEntity] that belong to the [profile].
 */
internal data class HttpProfileWithBioStyles(
  @Embedded val profile: HttpProfileEntity,
  @Relation(parentColumn = "id", entityColumn = "parent_id") val styles: List<HttpStyleEntity>
)
