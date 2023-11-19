package com.jeanbarrossilva.orca.platform.ui.component.stat

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.autos.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

@Composable
@Suppress("TestFunctionName")
internal fun TestActivateableStatIcon(
  modifier: Modifier = Modifier,
  isActive: Boolean = false,
  interactiveness: ActivateableStatIconInteractiveness = ActivateableStatIconInteractiveness.Still
) {
  ActivateableStatIcon(
    AutosTheme.iconography.forward.asImageVector,
    contentDescription = "Proceed",
    isActive,
    interactiveness,
    ActivateableStatIconColors(LocalContentColor.current, LocalContentColor.current),
    modifier
  )
}
