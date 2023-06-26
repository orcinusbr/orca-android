package com.jeanbarrossilva.mastodonte.feature.tootdetails

import androidx.compose.ui.text.AnnotatedString
import com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header.formatted
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.TootPreview
import com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot.relative
import java.io.Serializable
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Information relevant only for displaying a
 * [Toot][com.jeanbarrossilva.mastodonte.core.profile.toot.Toot]'s details.
 *
 * @param id Unique identifier.
 * @param avatarURL [URL] that leads to this [Toot]'s author's avatar.
 * @param name Name of this [Toot]'s author.
 * @param username Username of this [Toot]'s author.
 * @param body Content that's been written by the author.
 * @param publicationDateTime [ZonedDateTime] that informs when this [Toot] was published.
 * @param formattedPublicationDateTime Formatted, user-friendly, displayable version of
 * [publicationDateTime].
 * @param commentCount Amount of comments to this [Toot].
 * @param favoriteCount Amount of times that this [Toot] has been marked as favorite.
 * @param reblogCount Amount of times that this [Toot] has been reblogged.
 * @param url [URL] that leads to this [Toot].
 **/
internal data class Toot(
    val id: String,
    val avatarURL: URL,
    val name: String,
    val username: String,
    val body: AnnotatedString,
    private val publicationDateTime: ZonedDateTime,
    val formattedPublicationDateTime: String,
    val commentCount: String,
    val favoriteCount: String,
    val reblogCount: String,
    val url: URL
) : Serializable {
    /** Converts this [Toot] into a [TootPreview]. **/
    fun toTootPreview(): TootPreview {
        return TootPreview(
            avatarURL,
            name,
            username,
            timeSincePublication = publicationDateTime.relative,
            body,
            commentCount,
            favoriteCount,
            reblogCount
        )
    }

    companion object {
        /** [sample]'s [publicationDateTime]. **/
        private val samplePublicationDateTime =
            ZonedDateTime.of(2003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3"))

        /** Sample [Toot]. **/
        @Suppress("SpellCheckingInspection")
        val sample = Toot(
            id = "${UUID.randomUUID()}",
            TootPreview.sample.avatarURL,
            TootPreview.sample.name,
            TootPreview.sample.username,
            TootPreview.sample.body,
            samplePublicationDateTime,
            samplePublicationDateTime.formatted,
            commentCount = TootPreview.sample.commentCount,
            favoriteCount = TootPreview.sample.favoriteCount,
            reblogCount = TootPreview.sample.reblogCount,
            URL("https://mastodon.social/@christianselig/110492858891694580")
        )
    }
}
