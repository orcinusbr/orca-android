/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 */
internal fun browsesTo(uri: String): Matcher<Intent> {
  return both(hasAction(Intent.ACTION_VIEW)).and(hasData(uri))
}
