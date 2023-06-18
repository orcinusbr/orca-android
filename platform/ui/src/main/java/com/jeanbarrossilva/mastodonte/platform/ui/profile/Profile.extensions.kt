package com.jeanbarrossilva.mastodonte.platform.ui.profile

import com.jeanbarrossilva.mastodonte.core.profile.AnyProfile
import com.jeanbarrossilva.mastodonte.core.profile.Follow
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** A sample [Profile]. **/
val Profile.Companion.sample: AnyProfile
    get() = object : Profile<Follow.Public>() {
        override val id = UUID.randomUUID().toString()
        override val account = Author.sample.account
        override val avatarURL = Author.sample.avatarURL
        override val name = Author.sample.name

        @Suppress("SpellCheckingInspection")
        override val bio = "Engenheiro de software, autor, escritor e criador de conteúdo;" +
            "entusiasta da neurociência, da física quântica e da filosofia."

        override val follow = Follow.Public.unfollowed()
        override val followerCount = 0
        override val followingCount = 5
        override val url = Author.sample.profileURL

        override suspend fun onChangeFollowTo(follow: Follow.Public) {
        }

        override fun getToots(page: Int): Flow<List<Toot>> {
            return flowOf(Toot.samples)
        }
    }
