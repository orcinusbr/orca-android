package com.jeanbarrossilva.orca.feature.composer

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ComposerViewModel : ViewModel() {
  private val textFieldValueMutableFlow = MutableStateFlow(TextFieldValue())

  val textFieldValueFlow = textFieldValueMutableFlow.asStateFlow()

  fun setTextFieldValue(textFieldValue: TextFieldValue) {
    textFieldValueMutableFlow.value = textFieldValue
  }

  fun compose() {}
}
