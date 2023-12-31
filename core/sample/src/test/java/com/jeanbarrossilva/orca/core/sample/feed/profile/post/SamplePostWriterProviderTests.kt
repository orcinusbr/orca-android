/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import assertk.assertThat
import assertk.assertions.isSameAs
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import kotlin.test.Test
import org.junit.Rule

internal class SamplePostWriterProviderTests {
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule(Instance.sample)

  @Test(expected = SamplePostWriter.Provider.UnspecifiedWriterException::class)
  fun throwsWhenProvidingWithoutSpecifiedWriter() {
    SamplePostWriter.Provider().provide()
  }

  @Test
  fun providesWriterWhenItHasBeenProvided() {
    val writer = Instance.sample.postWriter
    assertThat(SamplePostWriter.Provider().apply { provide(writer) }.provide()).isSameAs(writer)
  }
}
