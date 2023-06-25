package com.jeanbarrossilva.mastodonte.core.profile.toot

import java.io.Serializable
import java.time.ZonedDateTime

/** Content that's been posted by a user, the [author]. **/
abstract class Toot : Serializable {
    /** Unique identifier. **/
    abstract val id: String

    /** [Author] that has authored this [Toot]. **/
    abstract val author: Author

    /** What's actually been written. **/
    abstract val content: String

    /** Zoned moment in time in which this [Toot] was published. **/
    abstract val publicationDateTime: ZonedDateTime

    /** Number of comments made in response to this [Toot]. **/
    abstract val commentCount: Int

    /** Number of favorites given by users to this [Toot]. **/
    abstract val favoriteCount: Int

    /** Number of times this [Toot] has been re-blogged. **/
    abstract val reblogCount: Int

    companion object
}
