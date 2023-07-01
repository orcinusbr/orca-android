package com.jeanbarrossilva.mastodonte.platform.ui.component.avatar.provider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.net.URL

/** Provides an [Avatar] image. **/
abstract class AvatarImageProvider {
    /** Loading stage in which the provided image can be. **/
    enum class State {
        /**
         * Nothing has happened and the image is yet to load.
         *
         * @see LOADING
         **/
        EMPTY,

        /**
         * The image is loading and may succeed or fail.
         *
         * @see LOADED
         * @see FAILED
         **/
        LOADING,

        /** The image has successfully been loaded. **/
        LOADED,

        /** The image could not be loaded due to an error. **/
        FAILED
    }

    /**
     * Provides an image that's located at the [url].
     *
     * @param name Name of the user to which the avatar belongs.
     * @param url [URL] that leads to the user's avatar image.
     * @param onStateChange Callback run whenever the image [State] is changed.
     * @param modifier [Modifier] to be applied to the underlying [Composable].
     **/
    @Composable
    @Suppress("ComposableNaming")
    internal abstract fun provide(
        name: String,
        url: URL,
        onStateChange: (State) -> Unit,
        modifier: Modifier
    )
}
