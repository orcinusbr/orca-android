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

package br.com.orcinus.orca.composite.timeline.post.time

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.mockkStatic
import io.mockk.verify
import java.time.ZonedDateTime
import kotlinx.coroutines.test.runTest
import net.time4j.android.ApplicationStarter
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class Time4JRelativeTimeProviderTests {
  @Test
  fun startsTime4JOnce() {
    val context: Context = InstrumentationRegistry.getInstrumentation().context
    val provider = Time4JRelativeTimeProvider(context)
    val zonedDateTime = ZonedDateTime.now()
    mockkStatic(ApplicationStarter::class) {
      runTest { repeat(2) { provider.provide(zonedDateTime) } }
      verify(exactly = 1) { ApplicationStarter.initialize(context, any<Boolean>()) }
    }
  }
}
