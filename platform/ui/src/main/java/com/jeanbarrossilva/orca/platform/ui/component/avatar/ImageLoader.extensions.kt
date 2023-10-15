package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.local.LocalImageLoader

/** [ImageLoader] that loads the sample avatar [Image]. */
internal val ImageLoader.Companion.avatar
    @Composable get() = object : LocalImageLoader() {
        override val context = LocalContext.current
        override val source = R.drawable.sample_avatar_default
    }
