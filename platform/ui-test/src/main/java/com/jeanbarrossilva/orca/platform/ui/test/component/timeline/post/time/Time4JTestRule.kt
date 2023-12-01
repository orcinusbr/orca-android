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

package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import net.time4j.android.ApplicationStarter
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that initializes the Time4J library before each test.
 *
 * @see ApplicationStarter.initialize
 */
class Time4JTestRule : ExternalResource() {
  override fun before() {
    val application = ApplicationProvider.getApplicationContext<Application>()
    ApplicationStarter.initialize(application)
  }
}
