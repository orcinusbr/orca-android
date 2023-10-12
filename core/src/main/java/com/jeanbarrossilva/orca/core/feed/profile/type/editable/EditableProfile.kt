package com.jeanbarrossilva.orca.core.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.Profile

/** [Profile] that can be edited through its [editor]. */
abstract class EditableProfile : Profile {
  /** [Editor] through which this [EditableProfile] can be edited. */
  abstract val editor: Editor

  companion object
}
