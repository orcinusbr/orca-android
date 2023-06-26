package com.jeanbarrossilva.mastodonte.core.inmemory.profile

import com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot.sample
import com.jeanbarrossilva.mastodonte.core.inmemory.profile.toot.samples
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.profile.edit.EditableProfile
import com.jeanbarrossilva.mastodonte.core.profile.edit.Editor
import com.jeanbarrossilva.mastodonte.core.profile.toot.Author
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** [sample]'s [ID][Profile.id]. **/
private val sampleID = UUID.randomUUID().toString()

/** A sample [Profile]. **/
val Profile.Companion.sample: Profile
    get() = object : EditableProfile() {
        override val id = sampleID
        override val account = Author.sample.account
        override val avatarURL = Author.sample.avatarURL
        override val name = Author.sample.name

        @Suppress("SpellCheckingInspection")
        override val bio = "Engenheiro de software, autor, escritor e criador de conteúdo; " +
            "entusiasta da neurociência, da física quântica e da filosofia."

        override val followerCount = 0
        override val followingCount = 5
        override val url = Author.sample.profileURL
        override val editor = Editor.empty

        override suspend fun getToots(page: Int): Flow<List<Toot>> {
            return flowOf(Toot.samples)
        }
    }
