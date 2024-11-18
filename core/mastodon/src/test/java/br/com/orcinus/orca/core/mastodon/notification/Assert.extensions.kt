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
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import assertk.Assert
import assertk.assertions.contains
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.google.common.collect.ImmutableList
import kotlin.reflect.KClass
import org.opentest4j.AssertionFailedError
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowApplication

/**
 * Asserts that the [Service] was the last one to be stopped.
 *
 * @param T [Service] on which the assertion is being performed.
 * @throws AssertionFailedError If the [Service] was not the last one to be stopped (even when it
 *   has been and is, in fact, stopped).
 * @see Context.stopService
 */
@Throws(AssertionFailedError::class)
internal fun <T : Service> Assert<KClass<T>>.isLastlyStopped(): Assert<KClass<T>> {
  given {
    assertThat<ShadowApplication>(
        shadowOf(ApplicationProvider.getApplicationContext<Application>())
      )
      .prop<_, Intent?, _>(ShadowApplication::getNextStoppedService)
      .isNotNull()
      .prop(Intent::getComponent)
      .isNotNull()
      .prop(ComponentName::getClassName)
      .isEqualTo(it.java.name)
  }
  return this
}

/**
 * Asserts that the [BroadcastReceiver] is registered.
 *
 * @param T [BroadcastReceiver] on which the assertion is being performed.
 * @throws AssertionFailedError If the [BroadcastReceiver] is unregistered.
 * @see Context.registerReceiver
 */
@Throws(AssertionFailedError::class)
internal inline fun <reified T : BroadcastReceiver> Assert<KClass<T>>.isRegistered():
  Assert<KClass<T>> {
  given {
    assertThat<ShadowApplication>(
        shadowOf(ApplicationProvider.getApplicationContext<Application>())
      )
      .prop<_, ImmutableList<ShadowApplication.Wrapper>, _>(
        ShadowApplication::getRegisteredReceivers
      )
      .extracting { (it.getBroadcastReceiver() as BroadcastReceiver)::class }
      .contains(T::class)
  }
  return this
}

/**
 * Creates an [Assert] on the amount of connections bound to the [Service].
 *
 * @param T [Service] on which the assertion is being performed.
 * @see Context.bindService
 */
internal inline fun <reified T : Service> Assert<KClass<T>>.bindingCount(): Assert<Int> {
  return transform("${T::class.simpleName} binding count") { kClass ->
    shadowOf(ApplicationProvider.getApplicationContext<Application>())
      ?.allStartedServices
      ?.filter { intent -> intent.component?.className == kClass.java.name }
      ?.size
      ?: 0
  }
}
