package com.jeanbarrossilva.mastodonte.platform.ui.component.stat

import androidx.compose.material.icons.rounded.Forward
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

@Composable
@Suppress("TestFunctionName")
internal fun TestActivateableStatIcon(
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    interactiveness: ActivateableStatIconInteractiveness = ActivateableStatIconInteractiveness.Still
) {
    ActivateableStatIcon(
        MastodonteTheme.Icons.Forward,
        contentDescription = "Proceed",
        isActive,
        interactiveness,
        ActivateableStatIconColors(LocalContentColor.current, LocalContentColor.current),
        modifier
    )
}
