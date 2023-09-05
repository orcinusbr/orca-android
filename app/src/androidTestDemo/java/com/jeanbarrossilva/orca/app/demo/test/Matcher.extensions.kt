package com.jeanbarrossilva.orca.app.demo.test

import android.content.Intent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import java.net.URI
import org.hamcrest.CoreMatchers.both
import org.hamcrest.Matcher

/**
 * Creates a [Matcher] that matches an [Intent] that browses to the given [uri].
 *
 * @param uri [String] form of the [URI] to which the [Intent] browses to.
 **/
internal fun browsesTo(uri: String): Matcher<Intent> {
    return both(hasAction(Intent.ACTION_VIEW)).and(hasData(uri))
}
