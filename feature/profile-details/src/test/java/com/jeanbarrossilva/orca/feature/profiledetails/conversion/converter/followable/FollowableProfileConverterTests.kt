package com.jeanbarrossilva.orca.feature.profiledetails.conversion.converter.followable

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.type.editable.EditableProfile
import com.jeanbarrossilva.orca.core.feed.profile.type.followable.FollowableProfile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.type.followable.sample
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
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
            ProfileDetails.Followable.createSample(Colors.Unspecified, onStatusToggle),
            converter.convert(FollowableProfile.sample, Colors.Unspecified)
                .let { it as ProfileDetails.Followable }
                .copy(onStatusToggle = onStatusToggle)
        )
    }

    @Test
    fun doesNotConvertDefaultProfile() {
        assertNull(converter.convert(Profile.sample, Colors.Unspecified))
    }

    @Test
    fun doesNotConvertEditableProfile() {
        assertNull(converter.convert(EditableProfile.sample, Colors.Unspecified))
    }
}
