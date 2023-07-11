package com.jeanbarrossilva.mastodon.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.edit.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.edit.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

internal class EditableProfileConverterTests {
    private val converter = EditableProfileConverter(next = null)

    @Test
    fun convertsEditableProfile() {
        assertEquals(ProfileDetails.Editable.sample, converter.convert(EditableProfile.sample))
    }

    @Test
    fun doesNotConvertDefaultProfile() {
        assertNull(converter.convert(Profile.sample))
    }

    @Test
    fun doesNotConvertFollowableProfile() {
        assertNull(converter.convert(FollowableProfile.sample))
    }
}
