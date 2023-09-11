package com.jeanbarrossilva.orca.platform.theme.configuration.iconography

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.jeanbarrossilva.orca.platform.theme.R
import com.jeanbarrossilva.orca.platform.theme.extensions.Empty

/** [CompositionLocal] that provides an [Iconography]. **/
internal val LocalIconography = compositionLocalOf {
    Iconography.Empty
}

/**
 * [Icon]s and [ImageVector]s that can be used throughout the app within various contexts.
 *
 * @param back [ImageVector] that represents back navigation.
 * @param comment [Icon] of a comment.
 * @param compose [Icon] for creation scenarios.
 * @param edit [Icon] that implies editing.
 * @param empty [ImageVector] that characterizes emptiness.
 * @param expand [ImageVector] for communicating possibility of expansion.
 * @param favorite [Icon] that signalizes a single or various "like" demonstrations.
 * @param forward [ImageVector] that represents forward navigation.
 * @param home [Icon] that portrays the start page.
 * @param link [ImageVector] for representing links.
 * @param login [ImageVector] that resembles a login action.
 * @param profile [Icon] that symbolizes the user's or someone else's account.
 * @param reblog [ImageVector] for a single or various reposts.
 * @param search [ImageVector] that indicates the ability to search or the performance of such
 * operation for elements in a given context.
 * @param send [ImageVector] that denotes a send operation.
 * @param share [Icon] for sharing.
 * @param unavailable [Icon] for unavailable resources.
 **/
data class Iconography internal constructor(
    val back: ImageVector,
    val comment: Icon,
    val compose: Icon,
    val edit: Icon,
    val empty: ImageVector,
    val expand: ImageVector,
    val favorite: Icon,
    val forward: ImageVector,
    val home: Icon,
    val link: ImageVector,
    val login: ImageVector,
    val profile: Icon,
    val reblog: ImageVector,
    val search: ImageVector,
    val send: ImageVector,
    val share: Icon,
    val unavailable: Icon
) {
    companion object {
        /** [Iconography] with empty values. **/
        internal val Empty = Iconography(
            back = ImageVector.Empty,
            comment = Icon.Empty,
            compose = Icon.Empty,
            edit = Icon.Empty,
            empty = ImageVector.Empty,
            expand = ImageVector.Empty,
            favorite = Icon.Empty,
            forward = ImageVector.Empty,
            home = Icon.Empty,
            link = ImageVector.Empty,
            login = ImageVector.Empty,
            profile = Icon.Empty,
            reblog = ImageVector.Empty,
            search = ImageVector.Empty,
            send = ImageVector.Empty,
            share = Icon.Empty,
            unavailable = Icon.Empty
        )

        /** [Iconography] that's provided by default. **/
        internal val default
            @Composable get() = Iconography(
                ImageVector.vectorResource(R.drawable.icon_back),
                Icon(
                    ImageVector.vectorResource(R.drawable.icon_comment_outlined),
                    ImageVector.vectorResource(R.drawable.icon_comment_filled)
                ),
                Icon(
                    ImageVector.vectorResource(R.drawable.icon_compose_outlined),
                    ImageVector.vectorResource(R.drawable.icon_compose_filled)
                ),
                Icon(
                    ImageVector.vectorResource(R.drawable.icon_edit_outlined),
                    ImageVector.vectorResource(R.drawable.icon_edit_filled)
                ),
                ImageVector.vectorResource(R.drawable.icon_empty),
                ImageVector.vectorResource(R.drawable.icon_expand),
                Icon(
                    ImageVector.vectorResource(R.drawable.icon_favorite_outlined),
                    ImageVector.vectorResource(R.drawable.icon_favorite_filled)
                ),
                ImageVector.vectorResource(R.drawable.icon_forward),
                Icon(
                    ImageVector.vectorResource(R.drawable.icon_home_outlined),
                    ImageVector.vectorResource(R.drawable.icon_home_filled)
                ),
                ImageVector.vectorResource(R.drawable.icon_link),
                ImageVector.vectorResource(R.drawable.icon_login),
                Icon(
                    ImageVector.vectorResource(R.drawable.icon_profile_outlined),
                    ImageVector.vectorResource(R.drawable.icon_profile_filled)
                ),
                ImageVector.vectorResource(R.drawable.icon_reblog),
                ImageVector.vectorResource(R.drawable.icon_search),
                ImageVector.vectorResource(R.drawable.icon_send),
                Icon(
                    ImageVector.vectorResource(R.drawable.icon_share_outlined),
                    ImageVector.vectorResource(R.drawable.icon_share_filled)
                ),
                Icon(
                    ImageVector.vectorResource(R.drawable.icon_unavailable_outlined),
                    ImageVector.vectorResource(R.drawable.icon_unavailable_filled)
                )
            )
    }
}
