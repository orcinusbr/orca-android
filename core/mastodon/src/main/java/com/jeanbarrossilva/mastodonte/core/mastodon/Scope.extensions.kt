package com.jeanbarrossilva.mastodonte.core.mastodon

import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

/** Creates or gets the existing instance of a [MastodonDatabase]. **/
val Scope.mastodonDatabase
    get() = MastodonDatabase.getInstance(androidContext())
