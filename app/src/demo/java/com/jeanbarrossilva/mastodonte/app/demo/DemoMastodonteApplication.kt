package com.jeanbarrossilva.mastodonte.app.demo

import com.jeanbarrossilva.mastodonte.app.MastodonteApplication
import com.jeanbarrossilva.mastodonte.core.profile.ProfileRepository
import com.jeanbarrossilva.mastodonte.platform.launchable.isFirstLaunch
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

internal class DemoMastodonteApplication : MastodonteApplication() {
    override fun onCreate() {
        super.onCreate()
        addSamplesOnFirstLaunch()
    }

    private fun addSamplesOnFirstLaunch() {
        if (isFirstLaunch) {
            MainScope().launch {
                addSamples()
            }
        }
    }

    private suspend fun addSamples() {
        val profileRepository by inject<ProfileRepository>()
    }

    companion object {
    }
}
