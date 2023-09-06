package com.jeanbarrossilva.orca.platform.ui.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.junit.Assert.assertNotNull

/**
 * Asserts that a [Fragment] tagged as [tag] is the current one within the given [activity].
 *
 * @param activity [FragmentActivity] in which the [Fragment] is supposed to be.
 * @param tag Tag of the [Fragment] that's supposed to be the current one.
 **/
fun assertIsAtFragment(activity: FragmentActivity, tag: String) {
    assertNotNull(
        "Fragment tagged as \"$tag\" not found.",
        activity.supportFragmentManager.findFragmentByTag(tag)
    )
}
