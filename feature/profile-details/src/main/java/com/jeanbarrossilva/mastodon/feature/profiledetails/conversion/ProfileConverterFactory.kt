package com.jeanbarrossilva.mastodon.feature.profiledetails.conversion

import com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.converter.DefaultProfileConverter
import com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.converter.EditableProfileConverter
import com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.converter.followable.FollowableProfileConverter
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import kotlinx.coroutines.CoroutineScope

/** Creates an instance of a [ProfileConverter] through [create]. **/
internal object ProfileConverterFactory {
    /**
     * Creates a [ProfileConverter].
     *
     * @param coroutineScope [CoroutineScope] through which converted [Profile]-related suspending
     * will be performed.
     **/
    fun create(coroutineScope: CoroutineScope): ProfileConverter {
        val defaultConverter = DefaultProfileConverter(next = null)
        val editableConverter = EditableProfileConverter(next = defaultConverter)
        return FollowableProfileConverter(coroutineScope, next = editableConverter)
    }
}
