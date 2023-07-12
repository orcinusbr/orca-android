package com.jeanbarrossilva.mastodonte.feature.profiledetails.conversion

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.edit.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.follow.FollowableProfile
import com.jeanbarrossilva.mastodonte.core.sample.profile.edit.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.follow.sample
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetails
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ProfileConverterFactoryTests {
    private val coroutineScope = TestScope()

    @Test
    fun createdConverterConvertsDefaultProfile() {
        assertEquals(
            ProfileDetails.Default.sample,
            ProfileConverterFactory.create(coroutineScope).convert(Profile.sample)
        )
    }

    @Test
    fun createdConverterConvertsEditableProfile() {
        assertEquals(
            ProfileDetails.Editable.sample,
            ProfileConverterFactory.create(coroutineScope).convert(EditableProfile.sample)
        )
    }

    @Test
    fun createdConverterConvertsFollowableProfile() {
        val onStatusToggle = { }
        assertEquals(
            ProfileDetails.Followable.createSample(onStatusToggle),
            ProfileConverterFactory
                .create(coroutineScope)
                .convert(FollowableProfile.sample)
                .let { it as ProfileDetails.Followable }
                .copy(onStatusToggle = onStatusToggle)
        )
    }
}
