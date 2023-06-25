package com.jeanbarrossilva.mastodonte.app

import com.jeanbarrossilva.mastodonte.core.inmemory.profile.InMemoryProfileDao
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MastodonteModule(): Module {
    return module {
        single<ProfileRepository> {
            InMemoryProfileDao
        }
    }
}
