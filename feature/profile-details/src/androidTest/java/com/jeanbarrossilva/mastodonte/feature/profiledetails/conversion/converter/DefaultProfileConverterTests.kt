package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.converter

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.edit.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.edit.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample
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
