/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.sharedpreferences.actor.mirror.image

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.core.sample.image.AuthorImageSource
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.uri.URIBuilder
import java.net.URI
import org.junit.Test

internal class ImageLoaderProviderFactoryTests {
  @Test(expected = ImageLoaderProviderFactory.UnknownSourceTypeException::class)
  fun throwsWhenFoldingUnknownSource() {
    ImageLoaderProviderFactory.fold<SampleImageSource, _>("", onURI = {}, onSampleImageSource = {})
  }

  @Test(expected = ImageLoaderProviderFactory.UnknownSourceTypeException::class)
  fun throwsWhenFoldingUnknownSourceClass() {
    ImageLoaderProviderFactory.fold(Nothing::class, onURI = {}, onSampleImageSource = {})
  }

  @Test
  fun runsURICallbackWhenSourceIsURI() {
    assertThat(
        ImageLoaderProviderFactory.fold<SampleImageSource, _>(
          URIBuilder.url()
            .scheme("https")
            .host("app.orca.jeanbarrossilva.com")
            .path("profile")
            .path("@jeanbarrossilva")
            .path("avatar.jpg")
            .build()
            .toString(),
          onURI = { 0 },
          onSampleImageSource = { 1 }
        )
      )
      .isEqualTo(0)
  }

  @Test
  fun runsURICallbackWhenSourceClassIsURI() {
    assertThat(
        ImageLoaderProviderFactory.fold(URI::class, onURI = { 0 }, onSampleImageSource = { 1 })
      )
      .isEqualTo(0)
  }

  @Test(expected = NoSuchFieldException::class)
  fun throwsWhenSourceIsSampleImageSourceButIsNotAnObject() {
    ImageLoaderProviderFactory.fold<SampleImageSource, _>(
      SampleImageSource::class.java.name,
      onSampleImageSource = {},
      onURI = {}
    )
  }

  @Test(expected = ClassCastException::class)
  fun throwsWhenSourceIsSampleImageSourceButTypeIsMismatched() {
    ImageLoaderProviderFactory.fold<SampleImageSource.None, _>(
      AuthorImageSource.Default::class.java.name,
      onSampleImageSource = {},
      onURI = {}
    )
  }

  @Test
  fun runsSampleImageSourceCallbackWhenSourceIsSampleImageSource() {
    assertThat(
        ImageLoaderProviderFactory.fold<SampleImageSource.None, _>(
          SampleImageSource.None::class.java.name,
          onURI = { 0 },
          onSampleImageSource = { 1 }
        )
      )
      .isEqualTo(1)
  }

  @Test
  fun runsSampleImageSourceCallbackWhenSourceClassIsSampleImageSource() {
    assertThat(
        ImageLoaderProviderFactory.fold(
          SampleImageSource::class,
          onURI = { 0 },
          onSampleImageSource = { 1 }
        )
      )
      .isEqualTo(1)
  }

  @Test
  fun runsSampleImageSourceCallbackWhenSourceIsSampleImageSourceSubclass() {
    assertThat(
        ImageLoaderProviderFactory.fold<AuthorImageSource.Default, _>(
          AuthorImageSource.Default::class.java.name,
          onURI = { 0 },
          onSampleImageSource = { 1 }
        )
      )
      .isEqualTo(1)
  }

  @Test
  fun runsSampleImageSourceCallbackWhenSourceClassIsSampleImageSourceSubclass() {
    assertThat(
        ImageLoaderProviderFactory.fold(
          AuthorImageSource.Default::class,
          onURI = { 0 },
          onSampleImageSource = { 1 }
        )
      )
      .isEqualTo(1)
  }

  @Test(expected = ImageLoaderProviderFactory.UnknownSourceTypeException::class)
  fun throwsWhenCreatingProviderForUnknownSourceClass() {
    NoOpImageLoaderProviderFactory.createFor(Nothing::class)
  }

  @Test
  fun createsProviderForURI() {
    assertThat(NoOpImageLoaderProviderFactory.createFor(URI::class))
      .isInstanceOf<SomeImageLoaderProvider<URI>>()
  }

  @Test
  fun createsProviderForSampleImageSource() {
    assertThat(NoOpImageLoaderProviderFactory.createFor(SampleImageSource::class))
      .isInstanceOf<SomeImageLoaderProvider<SampleImageSource>>()
  }
}
