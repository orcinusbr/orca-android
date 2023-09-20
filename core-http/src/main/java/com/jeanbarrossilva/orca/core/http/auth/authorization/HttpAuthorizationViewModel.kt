package com.jeanbarrossilva.orca.core.http.auth.authorization

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import io.ktor.http.Url

/**
 * [AndroidViewModel] that provides the [url] to be opened in the browser for authenticating the
 * user.
 *
 * @param application [Application] that allows [Context]-specific behavior.
 * @param lazyUrl [Lazy] version of the [Url] to be obtained only when it's referenced.
 **/
abstract class HttpAuthorizationViewModel(application: Application, lazyUrl: Lazy<Url>) :
    AndroidViewModel(application) {
    /** [Url] to be opened in order to authenticate. **/
    internal val url by lazyUrl
}
