package com.jeanbarrossilva.orca.platform.theme.ui.action.setting.group

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.theme.ui.action.setting.Setting

/** Scope of a [SettingGroup], through which [setting]s can be added. **/
class SettingGroupScope internal constructor() {
    /** [SnapshotStateList] of [SettingMetadata] from which the [Setting]s will be displayed. **/
    internal val metadata = mutableStateListOf<SettingMetadata>()

    /**
     * Creates metadata based on the given parameters for the equivalent [Setting] to be displayed.
     *
     * @param id [String] that uniquely identifies the metadata.
     * @param text [Text] to be shown by the [Setting].
     * @param action Action representation to be shown by the [Setting].
     * @param onClick Callback to be run when the [Setting] is clicked.
     * @param modifier [Modifier] to applied to the [Setting].
     **/
    fun setting(
        id: String,
        text: @Composable () -> Unit,
        action: @Composable () -> Unit,
        onClick: (() -> Unit)?,
        modifier: Modifier = Modifier
    ) {
        val metadata = SettingMetadata(id, modifier, text, action, onClick)
        val index = this.metadata.indexOf(metadata)
        val isExisting = index >= 0
        if (isExisting) replaceMetadataAt(index, metadata) else this.metadata.add(metadata)
    }

    /**
     * Replaces the [SettingMetadata] at the given [index] by [replacement].
     *
     * @param index Index of the [SettingMetadata] to be replaced by [replacement].
     * @param replacement [SettingMetadata] to replace the one at [index] by.
     **/
    private fun replaceMetadataAt(index: Int, replacement: SettingMetadata) {
        metadata.removeAt(index)
        metadata.add(index, replacement)
    }
}
