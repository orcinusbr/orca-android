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

    companion object
}
