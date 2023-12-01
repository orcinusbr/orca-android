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

package com.jeanbarrossilva.orca.platform.ui.core.image

import androidx.annotation.DrawableRes
import com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource
import com.jeanbarrossilva.orca.core.sample.image.CoverImageSource
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.platform.ui.R

/** Resource ID for this respective [SampleImageSource]. */
internal val SampleImageSource.resourceID
  @DrawableRes
  get() =
    when (this) {
      com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource.Default ->
        R.drawable.sample_avatar_default
      com.jeanbarrossilva.orca.core.sample.image.AuthorImageSource.Rambo ->
        R.drawable.sample_avatar_rambo
      com.jeanbarrossilva.orca.core.sample.image.CoverImageSource.Default ->
        R.drawable.sample_cover_default
      SampleImageSource.None -> -1
    }
