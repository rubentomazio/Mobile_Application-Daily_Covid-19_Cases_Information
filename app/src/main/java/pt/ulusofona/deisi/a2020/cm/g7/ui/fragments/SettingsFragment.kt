package pt.ulusofona.deisi.a2020.cm.g7.ui.fragments

import android.os.Bundle
import pt.ulusofona.deisi.a2020.cm.g7.R
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}