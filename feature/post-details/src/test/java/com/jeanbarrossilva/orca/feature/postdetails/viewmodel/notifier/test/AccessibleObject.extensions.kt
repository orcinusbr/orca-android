package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test

import kotlin.reflect.KCallable
import kotlin.reflect.jvm.isAccessible

/**
 * Makes this [KCallable] accessible for the given [access] and resets its accessibility to the
 * previous one when the [access] lambda has finished running.
 *
 * @param I [KCallable] to be accessed.
 * @param O Value returned by the [access].
 * @param access Access to be performed while this [KCallable] is ensured to be accessible.
 * @see KCallable.isAccessible
 */
internal fun <I : KCallable<*>, O> I.access(access: I.() -> O): O {
  val wasAccessible = isAccessible
  isAccessible = true
  return access().also { isAccessible = wasAccessible }
}
