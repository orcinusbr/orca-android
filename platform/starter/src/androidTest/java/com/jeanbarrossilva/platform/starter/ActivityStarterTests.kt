/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.platform.starter

import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.spyk
import io.mockk.verify
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ActivityStarterTests {
  private val context
    get() = InstrumentationRegistry.getInstrumentation().context

  internal class TestStartableActivity : StartableActivity()

  @Test
  fun startsActivity() {
    val spiedContext = spyk(context)
    spiedContext.on<TestStartableActivity>().asNewTask().start(StartableActivity::finish)
    verify { spiedContext.startActivity(any()) }
  }

  @Test
  fun notifiesListenerWhenActivityIsStarted() {
    runTest {
      @Suppress("RemoveExplicitTypeArguments")
      suspendCoroutine<TestStartableActivity> { continuation ->
        context.on<TestStartableActivity>().asNewTask().start { activity ->
          activity.finish()
          continuation.resume(activity)
        }
      }
    }
  }
}
