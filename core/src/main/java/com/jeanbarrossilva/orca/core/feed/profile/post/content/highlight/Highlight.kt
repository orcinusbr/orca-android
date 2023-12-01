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

package com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import java.net.URL

/**
 * Main portion of a [Content] that redirects the user to another site.
 *
 * @param headline [Headline] with the main information.
 * @param url [URL] that leads to the external site.
 */
data class Highlight(val headline: Headline, val url: URL) {
  companion object
}
