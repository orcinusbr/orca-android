package com.jeanbarrossilva.orca.platform.ui.test

import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import org.junit.Assert.assertNotNull
import org.robolectric.Shadows

/**
 * Asserts that a [Fragment] of type [T] is the current one within the given [activity].
 *
 * @param T [Fragment] to be asserted as the current one.
 * @param activity [FragmentActivity] in which the [Fragment] is supposed to be.
 **/
inline fun <reified T : Fragment> assertIsAt(activity: FragmentActivity) {
    val fragmentName = T::class.simpleName
    val tag = Navigator.tagFor<T>()
    Shadows.shadowOf(Looper.getMainLooper()).idle()
    assertNotNull(
        "$fragmentName not found. Make sure, if it has been navigated to, that its tag is the " +
            "result of Navigator.tagFor<$fragmentName>().",
        activity.supportFragmentManager.findFragmentByTag(tag)
    )
}
