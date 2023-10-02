package com.jeanbarrossilva.orca.core.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable.ToggleableStat
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

/** Content that's been posted by a user, the [author]. **/
abstract class Toot : Serializable {
    /** Unique identifier. **/
    abstract val id: String

    /** [Author] that has authored this [Toot]. **/
    abstract val author: Author

    /** [Content] that's been composed by the [author]. **/
    abstract val content: Content

    /** Zoned moment in time in which this [Toot] was published. **/
    abstract val publicationDateTime: ZonedDateTime

    /** [Stat] for comments. **/
    abstract val comment: Stat<Toot>

    /** [Stat] for favorites. **/
    abstract val favorite: ToggleableStat<Profile>

    /** [Stat] for reblogs. **/
    abstract val reblog: ToggleableStat<Profile>

    /** [URL] that leads to this [Toot]. **/
    abstract val url: URL

    companion object
}
