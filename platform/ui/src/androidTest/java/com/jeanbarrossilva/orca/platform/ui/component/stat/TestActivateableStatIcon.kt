package com.jeanbarrossilva.orca.platform.ui.component.stat

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

@Composable
@Suppress("TestFunctionName")
internal fun TestActivateableStatIcon(
  modifier: Modifier = Modifier,
  isActive: Boolean = false,
  interactiveness: ActivateableStatIconInteractiveness = ActivateableStatIconInteractiveness.Still
) {
  ActivateableStatIcon(
    OrcaTheme.iconography.forward,
    contentDescription = "Proceed",
    isActive,
    interactiveness,
    ActivateableStatIconColors(LocalContentColor.current, LocalContentColor.current),
    modifier
  )
}
