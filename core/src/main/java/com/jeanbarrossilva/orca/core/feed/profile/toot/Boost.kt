package com.jeanbarrossilva.orca.core.feed.profile.toot

import java.util.Objects

/** [Toot] that has been boosted by someone else. **/
abstract class Boost internal constructor() : Toot() {
    /** [Author] by which this [Boost] has been created. **/
    abstract val booster: Author

    override fun equals(other: Any?): Boolean {
        return other is Boost &&
            id == other.id &&
            author == other.author &&
            booster == other.booster &&
            content == other.content &&
            publicationDateTime == other.publicationDateTime &&
            commentCount == other.commentCount &&
            isFavorite == other.isFavorite &&
            favoriteCount == other.favoriteCount &&
            isReblogged == other.isReblogged &&
            reblogCount == other.reblogCount &&
            url == other.url
    }

    override fun hashCode(): Int {
        return Objects.hash(
            author,
            booster,
            content,
            publicationDateTime,
            commentCount,
            isFavorite,
            favoriteCount,
            isReblogged,
            reblogCount,
            url
        )
    }
}
