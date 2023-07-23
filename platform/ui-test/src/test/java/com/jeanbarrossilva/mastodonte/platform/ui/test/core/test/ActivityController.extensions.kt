package com.jeanbarrossilva.mastodonte.platform.ui.test.core.test

import com.jeanbarrossilva.mastodonte.platform.ui.test.core.SingleFragmentActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.robolectric.android.controller.ActivityController

/**
 * Suspends until the [SingleFragmentActivity]'s
 * [navGraphIntegrityInsuranceJob][SingleFragmentActivity.navGraphIntegrityInsuranceJob] is
 * completed.
 **/
internal suspend fun <T : SingleFragmentActivity> ActivityController<T>.waitForNavGraphIntegrityInsurance(): // ktlint-disable max-line-length
    ActivityController<T> {
    val job = get().navGraphIntegrityInsuranceJob
    return if (job != null && job.isActive) {
        suspendCoroutine { continuation ->
            job.invokeOnCompletion {
                continuation.resume(this)
            }
        }
    } else {
        this
    }
}
