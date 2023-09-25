package com.jeanbarrossilva.orca.platform.theme.kit.input.option.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.border
import com.jeanbarrossilva.orca.platform.theme.extensions.bottom
import com.jeanbarrossilva.orca.platform.theme.extensions.top
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.Option
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.OptionDefaults

/** Tag that identifies [Options]' [Divider]s for testing purposes. **/
internal const val OPTIONS_DIVIDER_TAG = "options-divider"

/**
 * [Column] of loading [Option]s that are shaped according to their position.
 *
 * @param modifier [Modifier] to be applied to the underlying [Column].
 **/
@Composable
fun Options(modifier: Modifier = Modifier) {
    Options(onSelectionToggle = { _, _ -> }, modifier) {
        repeat(128) {
            option()
        }
    }
}

/**
 * [Column] of [Option]s that are shaped according to their position and can be singly selected.
 *
 * @param onSelectionToggle Callback run whenever any of the [Option]s is toggled.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param content Actions to be run on the given [OptionsScope].
 **/
@Composable
fun Options(
    onSelectionToggle: (index: Int, isSelected: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: OptionsScope.() -> Unit
) {
    val defaultOptionShape = OptionDefaults.shape
    val scope =
        remember(onSelectionToggle, content) { OptionsScope(onSelectionToggle).apply(content) }

    Column(modifier.border(defaultOptionShape)) {
        scope.options.forEachIndexed { index, option ->
            when {
                scope.options.size == 1 -> option(defaultOptionShape)
                index == 0 -> option(defaultOptionShape.top)
                index == scope.options.lastIndex -> {
                    option(defaultOptionShape.bottom)
                    return@forEachIndexed
                }
                else -> option(RectangleShape)
            }

            Divider(Modifier.testTag(OPTIONS_DIVIDER_TAG))
        }
    }
}

/**
 * [Column] of [Option]s that are shaped according to their position.
 *
 * @param count Amount of sample [Option]s to be added.
 * @param onSelectionToggle Callback run whenever any of the [Option]s is toggled.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 **/
@Composable
internal fun SampleOptions(
    modifier: Modifier = Modifier,
    count: Int = 3,
    onSelectionToggle: (index: Int, isSelected: Boolean) -> Unit = { _, _ -> }
) {
    Options(onSelectionToggle, modifier) {
        repeat(count) {
            option {
                Text("Label #$it")
            }
        }
    }
}

@Composable
@MultiThemePreview
private fun LoadingOptionsPreview() {
    OrcaTheme {
        Options()
    }
}

@Composable
@MultiThemePreview
private fun LoadedOptionsPreview() {
    OrcaTheme {
        SampleOptions()
    }
}
