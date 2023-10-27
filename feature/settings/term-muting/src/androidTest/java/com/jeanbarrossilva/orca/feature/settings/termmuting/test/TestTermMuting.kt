package com.jeanbarrossilva.orca.feature.settings.termmuting.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMuting

@Composable
@Suppress("TestFunctionName")
internal fun TestTermMuting(modifier: Modifier = Modifier) {
  var term by remember { mutableStateOf("") }
  TermMuting(modifier, term, onTermChange = { term = it }, onMute = {}, onPop = {})
}
