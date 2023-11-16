package com.jeanbarrossilva.orca.std.imageloader

import java.awt.Color
import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class ImageTests {
  @Test(expected = IllegalArgumentException::class)
  fun `GIVEN an image WHEN adding an insufficient amount of pixels to it THEN it throws`() {
    buildImage(width = 2, height = 2) { pixel(Color.BLACK.rgb) }
  }

  @Test(expected = IllegalArgumentException::class)
  fun `GIVEN an image WHEN adding more pixels than it can hold THEN it throws`() {
    buildImage(width = 1, height = 1) { repeat(2) { pixel(Color.BLACK.rgb) } }
  }

  @Test
  fun `GIVEN an image WHEN adding pixels to it THEN they're placed in the right coordinates`() {
    assertContentEquals(
      arrayOf(
        Image.Pixel(x = 0, y = 0, Color.BLACK.rgb),
        Image.Pixel(x = 1, y = 0, Color.BLACK.rgb),
        Image.Pixel(x = 0, y = 1, Color.BLACK.rgb),
        Image.Pixel(x = 1, y = 1, Color.BLACK.rgb)
      ),
      buildImage(width = 2, height = 2) { repeat(4) { pixel(Color.BLACK.rgb) } }.pixels
    )
  }
}
