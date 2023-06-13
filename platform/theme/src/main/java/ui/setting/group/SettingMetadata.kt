package com.jeanbarrossilva.mastodonte.platform.theme.ui.setting.group

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.mastodonte.platform.theme.ui.setting.Setting
import java.util.Objects

/**
 * Structure that holds the parameters of a [Setting].
 *
 * @param id [String] that uniquely identifies this [SettingMetadata].
 * @param modifier [Modifier] to applied to the [Setting].
 * @param text [Text] to be shown by the [Setting].
 * @param action Action representation to be shown by the [Setting].
 * @param onClick Callback to be run when the [Setting] is clicked.
 **/
internal data class SettingMetadata(
    val id: String,
    val modifier: Modifier,
    val text: @Composable () -> Unit,
    val action: @Composable () -> Unit,
    val onClick: (() -> Unit)?
) {
    override fun equals(other: Any?): Boolean {
        return other is SettingMetadata && id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id, modifier, text, action, onClick)
    }
}
