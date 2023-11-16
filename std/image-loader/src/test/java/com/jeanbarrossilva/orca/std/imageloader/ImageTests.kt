package com.jeanbarrossilva.orca.std.imageloader

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import java.awt.Color
import kotlin.test.Test

internal class ImageTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenPixelsAreInsufficient() {
    buildImage(width = 2, height = 2) { pixel(Color.BLACK.rgb) }
  }

  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenAddingMorePixelsThanTheSizeCanHold() {
    buildImage(width = 1, height = 1) { repeat(2) { pixel(Color.BLACK.rgb) } }
  }

  @Test
  fun addsPixelsToTheirPrecedingCoordinates() {
    assertThat(buildImage(width = 2, height = 2) { repeat(4) { pixel(Color.BLACK.rgb) } }.pixels)
      .containsExactly(
        Image.Pixel(x = 0, y = 0, Color.BLACK.rgb),
        Image.Pixel(x = 1, y = 0, Color.BLACK.rgb),
        Image.Pixel(x = 0, y = 1, Color.BLACK.rgb),
        Image.Pixel(x = 1, y = 1, Color.BLACK.rgb)
      )
  }

  @Test
  fun widthIsTheSameAsSpecifiedWhenBuildingTheImage() {
    assertThat(buildImage(width = 2, height = 1) { repeat(2) { pixel(Color.BLACK.rgb) } }.width)
      .isEqualTo(2)
  }

  @Test
  fun heightIsTheSameAsSpecifiedWhenBuildingTheImage() {
    assertThat(buildImage(width = 1, height = 2) { repeat(2) { pixel(Color.BLACK.rgb) } }.height)
      .isEqualTo(2)
  }
}
