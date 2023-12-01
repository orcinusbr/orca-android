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

package com.jeanbarrossilva.orca.app.demo.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.instance.createSample
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector

internal object DemoCoreModule :
  CoreModule(
    { DemoCoreModule.instanceProvider },
    { DemoCoreModule.instanceProvider.provide().authenticationLock },
    { SampleTermMuter() }
  ) {
  private val instanceProvider by lazy { InstanceProvider.createSample(imageLoaderProvider) }

  private val imageLoaderProvider
    get() = ImageLoader.Provider.createSample(context)

  private val context
    get() = Injector.get<Context>()
}
