package com.jeanbarrossilva.mastodonte.feature.auth.test

import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.mastodonte.core.test.TestActorProvider
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun AuthModule(): Module {
    return module {
        single<Authenticator> {
            SampleAuthenticator(TestActorProvider())
        }
    }
}
