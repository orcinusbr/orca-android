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

package com.jeanbarrossilva.orca.platform.autos.theme

import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper
import com.jeanbarrossilva.orca.platform.autos.R

/** [ContextThemeWrapper] with [R.style.Theme_Autos] as its theme. */
internal class AutosContextThemeWrapper(context: Context) :
  ContextThemeWrapper(context, R.style.Theme_Autos)
