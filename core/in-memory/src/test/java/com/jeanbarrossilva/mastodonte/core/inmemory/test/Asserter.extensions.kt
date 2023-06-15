package com.jeanbarrossilva.mastodonte.core.inmemory.test

import com.jeanbarrossilva.mastodonte.core.inmemory.profile.InMemoryProfile
import com.jeanbarrossilva.mastodonte.core.inmemory.profile.InMemoryProfileDao
import com.jeanbarrossilva.mastodonte.core.profile.`@`
import com.jeanbarrossilva.mastodonte.core.profile.Follow
import java.net.URL
import java.util.UUID
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest

/**
 * Asserts that toggling an [InMemoryProfile]'s [follow][InMemoryProfile.follow] status that's been
 * initially set to [before] results in [after].
 *
 * @param before [Follow] status before the [toggle][InMemoryProfile.toggleFollow].
 * @param after [Follow] status after the [toggle][InMemoryProfile.toggleFollow].
 **/
internal fun <T : Follow> assertTogglingEquals(before: T, after: T) {
    val matchingAfter = Follow.requireVisibilityMatch(before, after)
    val id = UUID.randomUUID().toString()
    InMemoryProfileDao.add(
        id,
        account = "john" `@` "appleseed.com",
        avatarURL = URL("https://appleseed.john.com/avatar.png"),
        name = "John Appleseed",
        bio = "Not a real person.",
        follow = before,
        followerCount = 0,
        followingCount = 0,
        URL("https://appleseed.john.com")
    )
    runTest {
        assertEquals(
            matchingAfter,
            InMemoryProfileDao
                .get(id)
                .filterNotNull()
                .onEach { it.toggleFollow() }
                .first()
                .follow
        )
    }
}
