package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.type.editable.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.type.editable.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.type.followable.sample
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetails
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

internal class FollowableProfileConverterTests {
    private val coroutineScope = TestScope()
    private val converter = FollowableProfileConverter(coroutineScope, next = null)

    @Test
    fun convertsFollowableProfile() {
        val onStatusToggle = { }
        assertEquals(
            ProfileDetails.Followable.createSample(onStatusToggle),
            (converter.convert(FollowableProfile.sample) as ProfileDetails.Followable).copy(
                onStatusToggle = onStatusToggle
            )
        )
    }

    @Test
    fun doesNotConvertDefaultProfile() {
        assertNull(converter.convert(Profile.sample))
    }

    @Test
    fun doesNotConvertEditableProfile() {
        assertNull(converter.convert(EditableProfile.sample))
    }
}
