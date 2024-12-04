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

package br.com.orcinus.orca.core.mastodon.notification.push

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import assertk.Assert
import assertk.assertions.contains
import kotlin.reflect.KClass
import org.robolectric.Shadows.shadowOf

/**
 * Creates an [Assert] on the amount of connections bound to the [Service].
 *
 * @param T [Service] on which the assertion is being performed.
 * @see Context.bindService
 */
internal inline fun <reified T : Service> Assert<KClass<T>>.bindingCount(): Assert<Int> {
  return transform("${T::class.simpleName} binding count") { kClass ->
    val application = ApplicationProvider.getApplicationContext<Application>()
    val shadowApplication = shadowOf(application) ?: return@transform 0
    val isStartableBy = { intent: Intent -> intent.component?.className == kClass.java.name }
    var stoppingCount = 0
    while (true) {
      shadowApplication.nextStoppedService?.let {
        if (isStartableBy(it)) {
          stoppingCount++
        }
      }
        ?: break
    }
    shadowApplication.allStartedServices?.count(isStartableBy)?.minus(stoppingCount) ?: 0
  }
}
