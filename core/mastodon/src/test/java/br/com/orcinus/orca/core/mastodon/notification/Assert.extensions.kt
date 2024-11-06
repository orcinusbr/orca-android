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

package br.com.orcinus.orca.core.mastodon.notification

import android.app.Application
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import kotlin.reflect.KClass
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowApplication

/**
 * Creates an [Assert] on the [KClass] of [T].
 *
 * @param T Object on whose [KClass] assertions are to be performed.
 */
internal inline fun <reified T : Any> assertThat() = assertk.assertThat(T::class)

/**
 * Asserts that the [Service] was the last one to be stopped.
 *
 * @param T [Service] on which the assertion is being performed.
 * @see Context.stopService
 */
internal fun <T : Service> Assert<KClass<T>>.isLastlyStopped(): Assert<KClass<T>> {
  given { kClass ->
    assertThat<ShadowApplication>(
        shadowOf(ApplicationProvider.getApplicationContext<Application>())
      )
      .prop<_, Intent?, _>(ShadowApplication::getNextStoppedService)
      .isNotNull()
      .prop(Intent::getComponent)
      .isNotNull()
      .prop(ComponentName::getClassName)
      .isEqualTo(kClass.java.name)
  }
  return this
}
