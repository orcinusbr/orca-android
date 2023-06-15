package com.jeanbarrossilva.mastodonte.core.profile.toot

/** Content that's been posted by a user, the [author]. **/
abstract class Toot {
    /** Unique identifier. **/
    abstract val id: String

    /** [Author] that has authored this [Toot]. **/
    abstract val author: Author

    /** What's actually been written. **/
    abstract val content: String
}
