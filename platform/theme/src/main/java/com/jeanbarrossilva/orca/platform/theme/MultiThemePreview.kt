package com.jeanbarrossilva.orca.platform.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/** [Preview] with all themes supported by Orca. **/
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
annotation class MultiThemePreview
