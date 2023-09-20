package com.jeanbarrossilva.orca.core.mastodon.auth.authentication.activity

import android.content.Context
import androidx.compose.runtime.Composable
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticationActivity
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.Authentication
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.MastodonAuthenticationViewModel
import com.jeanbarrossilva.orca.platform.ui.core.ActivityStarter
import com.jeanbarrossilva.orca.platform.ui.core.on

class MastodonAuthenticationActivity internal constructor() :
    HttpAuthenticationActivity<MastodonAuthenticationViewModel>() {
    private val authorizationCode by extra<String>(AUTHORIZATION_CODE_KEY)

    override val viewModelClass = MastodonAuthenticationViewModel::class
    override val viewModelFactory by lazy {
        MastodonAuthenticationViewModel.createFactory(application, authorizationCode)
    }

    @Composable
    override fun Content() {
        Authentication()
    }

    companion object {
        private const val AUTHORIZATION_CODE_KEY = "authorization-code"

        fun getStarter(context: Context, authorizationCode: String):
            ActivityStarter<MastodonAuthenticationActivity> {
            return context.on<MastodonAuthenticationActivity>().with(
                AUTHORIZATION_CODE_KEY to authorizationCode
            )
        }
    }
}
