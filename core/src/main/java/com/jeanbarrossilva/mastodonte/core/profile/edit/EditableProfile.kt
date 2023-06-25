package com.jeanbarrossilva.mastodonte.core.profile.edit

import com.jeanbarrossilva.mastodonte.core.profile.Profile

/** [Profile] that can be edited through its [editor]. **/
abstract class EditableProfile : Profile {
    /** [Editor] through which this [EditableProfile] can be edited. **/
    abstract val editor: Editor
}
