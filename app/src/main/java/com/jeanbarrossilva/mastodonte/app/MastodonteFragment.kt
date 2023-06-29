package com.jeanbarrossilva.mastodonte.app

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.sample.profile.sample

internal class MastodonteFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        redirect()
    }

    private fun redirect() {
        val directions = MastodonteFragmentDirections.toProfileDetails().apply {
            arguments.putString("id", Profile.sample.id)
        }
        findNavController().navigate(directions)
    }
}
