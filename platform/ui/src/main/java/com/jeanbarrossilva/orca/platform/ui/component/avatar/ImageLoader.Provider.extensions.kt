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

package com.jeanbarrossilva.orca.platform.ui.component.avatar

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.platform.ui.core.image.PlatformSampleImageLoader
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [ImageLoader.Provider] that provides an [ImageLoader] by which [Image]s will be
 * loaded from a [SampleImageSource].
 */
@Composable
fun ImageLoader.Provider.Companion.createSample(): ImageLoader.Provider<SampleImageSource> {
  return createSample(LocalContext.current)
}

/**
 * Creates a sample [ImageLoader.Provider] that provides an [ImageLoader] by which [Image]s will be
 * loaded from a [SampleImageSource].
 *
 * @param context [Context] with which the underlying [PlatformSampleImageLoader.Provider] will be
 *   instantiated.
 */
fun ImageLoader.Provider.Companion.createSample(
  context: Context
): ImageLoader.Provider<SampleImageSource> {
  return PlatformSampleImageLoader.Provider(context)
}
