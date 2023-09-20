package com.jeanbarrossilva.orca.core.mastodon.auth.authorization

import android.net.Uri
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizationActivity

class MastodonAuthorizationActivity internal constructor() :
    HttpAuthorizationActivity<MastodonAuthorizationViewModel>() {
    override val viewModelClass = MastodonAuthorizationViewModel::class
    override val viewModelFactory by lazy {
        MastodonAuthorizationViewModel.createFactory(application)
    }

    override fun getAccessToken(deepLink: Uri): String? {
        return deepLink.getQueryParameter("code")
    }
}
