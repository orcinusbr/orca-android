package com.jeanbarrossilva.orca.feature.auth.test

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.orca.core.test.TestActorProvider
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
