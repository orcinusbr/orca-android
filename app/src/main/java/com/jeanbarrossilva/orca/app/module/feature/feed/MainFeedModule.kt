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

package com.jeanbarrossilva.orca.app.module.feature.feed

import com.jeanbarrossilva.orca.app.OrcaActivity
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.feature.feed.FeedModule
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainFeedModule(activity: OrcaActivity) :
  FeedModule(
    { Injector.from<CoreModule>().instanceProvider().provide().feedProvider },
    { Injector.from<CoreModule>().instanceProvider().provide().postProvider },
    { NavigatorFeedBoundary(activity, activity.navigator) },
    onBottomAreaAvailabilityChangeListener = { activity }
  )
