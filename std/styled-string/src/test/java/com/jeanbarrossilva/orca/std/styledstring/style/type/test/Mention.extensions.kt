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

package com.jeanbarrossilva.orca.std.styledstring.style.type.test

import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention
import java.net.URL

/**
 * Sample [URL] to create mentions with.
 *
 * @see StyledString.Builder.mention
 * @see Mention
 */
internal val Mention.Companion.url
  get() = URL("https://mastodon.social/@jeanbarrossilva")

/**
 * Sample username to mention.
 *
 * @see StyledString.Builder.mention
 * @see Mention
 */
internal val Mention.Companion.username
  get() = "jeanbarrossilva"
