package com.jeanbarrossilva.orca.platform.theme.kit.input.option.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.CoreOption
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.Option

/**
 * Scope through which [Option]s can be added.
 *
 * @param onSelectionToggle Callback run whenever any of the [Option]s is toggled.
 **/
class OptionsScope internal constructor(
    private val onSelectionToggle: (index: Int, isSelected: Boolean) -> Unit
) {
    /** Index of the [Option] that's currently selected. **/
    private var selectedOptionIndex by mutableIntStateOf(-1)

    /** [Option]s that have been added. **/
    internal val options = mutableStateListOf<@Composable (Shape) -> Unit>()

    /**
     * Adds an [Option].
     *
     * @param modifier [Modifier] to be applied to the [Option].
     * @param content [Text] that's the label.
     **/
    fun option(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
        with(options.size) index@{
            options.add { shape ->
                CoreOption(
                    isSelected = this@index == selectedOptionIndex,
                    onSelectionToggle = { isSelected ->
                        if (selectedOptionIndex != this@index) {
                            selectedOptionIndex = this@index
                            onSelectionToggle(selectedOptionIndex, isSelected)
                        }
                    },
                    modifier,
                    shape,
                    content
                )
            }
            if (this@index == 0) {
                selectedOptionIndex = 0
                onSelectionToggle(selectedOptionIndex, true)
            }
        }
    }

    /**
     * Adds a loading [Option].
     *
     * @param modifier [Modifier] to be applied to the [Option].
     **/
    internal fun option(modifier: Modifier = Modifier) {
        options.add {
            CoreOption(modifier, shape = it)
        }
    }
}
