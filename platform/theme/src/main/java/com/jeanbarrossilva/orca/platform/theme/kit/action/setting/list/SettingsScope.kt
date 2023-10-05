package com.jeanbarrossilva.orca.platform.theme.kit.action.setting.list

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.ActionScope
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.Group
import com.jeanbarrossilva.orca.platform.theme.kit.action.setting.Setting

/**
 * Scope through which [Setting]s can be added.
 *
 * @param onClick Callback run whenever a [Setting] is clicked.
 **/
class SettingsScope internal constructor(private val onClick: (index: Int) -> Unit) {
    /** [Setting]s that have been added. **/
    private val mutableSettings = mutableStateListOf<@Composable (Modifier, Shape) -> Unit>()

    /** Immutable [List] with the [Setting]s that have been added. **/
    internal val settings
        get() = mutableSettings.toList()

    /**
     * Adds a [Group].
     *
     * @param icon [Icon] that visually represents what it does.
     * @param label Short description of what it's for.
     * @param modifier [Modifier] to be applied to the [Group].
     * @param isInitiallyExpanded Whether it is expanded by default.
     **/
    fun group(
        icon: @Composable () -> Unit,
        label: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        isInitiallyExpanded: Boolean = false,
        content: SettingsScope.() -> Unit
    ) {
        with(mutableSettings.size) index@{
            mutableSettings.add { parentModifier, shape ->
                Group(
                    onClick = { onClick(this@index) },
                    icon,
                    label,
                    parentModifier then modifier,
                    isInitiallyExpanded,
                    shape as? CornerBasedShape ?: RoundedCornerShape(0),
                    content
                )
            }
        }
    }

    /**
     * Adds a [Setting].
     *
     * @param label Short description of what it's for.
     * @param modifier [Modifier] to be applied to the [Setting].
     * @param icon [Icon] that visually represents what it does.
     * @param action Portrays the result of invoking [onClick] or executes a related action.
     **/
    fun setting(
        label: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit = { },
        action: ActionScope.() -> Unit = { }
    ) {
        with(mutableSettings.size) index@{
            mutableSettings.add { parentModifier, shape ->
                Setting(
                    onClick = { onClick(this@index) },
                    label,
                    parentModifier then modifier,
                    shape,
                    icon,
                    action
                )
            }
        }
    }
}
