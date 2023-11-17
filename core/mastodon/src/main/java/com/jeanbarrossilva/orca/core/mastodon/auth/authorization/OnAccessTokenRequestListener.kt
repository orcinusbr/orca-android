package com.jeanbarrossilva.orca.core.mastodon.auth.authorization

/** Listens to requests for an access token. */
internal interface OnAccessTokenRequestListener {
  /** Callback run when the access token is requested. */
  fun onAccessTokenRequest()
}
