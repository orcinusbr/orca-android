package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.type.editable.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.type.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.type.editable.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.type.follow.sample
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetails
import org.junit.Assert.assertEquals
import org.junit.Test

internal class DefaultProfileConverterTests {
    private val converter = DefaultProfileConverter(next = null)

    @Test
    fun convertsDefaultProfile() {
        assertEquals(ProfileDetails.Default.sample, converter.convert(Profile.sample))
    }

    @Test
    fun convertsEditableProfile() {
        assertEquals(ProfileDetails.Default.sample, converter.convert(EditableProfile.sample))
    }

    @Test
    fun convertsFollowableProfile() {
        assertEquals(ProfileDetails.Default.sample, converter.convert(FollowableProfile.sample))
    }
}
