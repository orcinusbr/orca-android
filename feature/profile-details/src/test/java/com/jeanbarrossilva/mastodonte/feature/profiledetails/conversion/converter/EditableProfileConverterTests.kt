package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.mastodonte.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.sample
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.type.editable.sample
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.type.followable.sample
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetails
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
