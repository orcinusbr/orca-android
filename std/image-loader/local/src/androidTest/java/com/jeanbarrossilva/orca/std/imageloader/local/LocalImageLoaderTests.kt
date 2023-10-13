package com.jeanbarrossilva.orca.std.imageloader.local

import android.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.imageloader.buildImage
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class LocalImageLoaderTests {
  @Test
  fun loads() {
    val context = InstrumentationRegistry.getInstrumentation().context
    val imageLoader = LocalImageLoader(context, R.drawable.ic_white)
    runTest {
      assertThat(imageLoader.load(width = 1, height = 1))
        .isEqualTo(buildImage(width = 1, height = 1) { pixel(Color.WHITE) })
    }
  }
}
