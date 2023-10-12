package com.jeanbarrossilva.orca.core.http.auth.authorization

/** Listens to requests for an access token. */
internal interface OnAccessTokenRequestListener {
  /** Callback run when the access token is requested. */
  fun onAccessTokenRequest()
}
