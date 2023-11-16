package com.jeanbarrossilva.orca.std.imageloader.local

import android.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class LocalImageLoaderTests {
  @Test
  fun loads() {
    val imageLoader =
      object : LocalImageLoader() {
        override val context = InstrumentationRegistry.getInstrumentation().context
        override val source = R.drawable.ic_white
      }
    runTest {
      assertThat(imageLoader.load(width = 1, height = 1)?.pixels.orEmpty()).all {
        hasSize(1)
        given { assertThat(it.single().x).isEqualTo(0) }
        given { assertThat(it.single().y).isEqualTo(0) }
        given { assertThat(it.single().color).isEqualTo(Color.WHITE) }
      }
    }
  }
}
