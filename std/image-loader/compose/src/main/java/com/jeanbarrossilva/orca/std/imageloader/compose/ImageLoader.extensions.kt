package com.jeanbarrossilva.orca.std.imageloader.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/** Remembers an [ImageLoader]. **/
@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember(context) { CoilImageLoader(context) }
}
