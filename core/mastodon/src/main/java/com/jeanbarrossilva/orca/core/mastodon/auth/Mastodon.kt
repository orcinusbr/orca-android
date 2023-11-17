package com.jeanbarrossilva.orca.core.mastodon.auth

import com.jeanbarrossilva.orca.core.mastodon.BuildConfig

/** API configuration for authorization and authentication. */
internal object Mastodon {
  /** Identifies Orca amongst all Mastodon clients. */
  @Suppress("SpellCheckingInspection")
  const val CLIENT_ID = "F2Rx9d7C3x45KRVJ9rU4IjIJgrsjzaq74bSLo__VUG0"

  /** Private code. */
  const val CLIENT_SECRET = BuildConfig.mastodonclientSecret

  /** Scopes required by Orca for its functionalities to work properly. */
  const val SCOPES = "read write follow"
}
