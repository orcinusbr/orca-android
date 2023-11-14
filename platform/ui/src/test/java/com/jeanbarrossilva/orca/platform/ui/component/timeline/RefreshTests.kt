package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.compose.ui.unit.Dp
import org.junit.Test

internal class RefreshTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenIndicatorOffsetIsUnspecified() {
    @Suppress("UnusedDataClassCopyResult") Refresh.empty.copy(indicatorOffset = Dp.Unspecified)
  }
}
