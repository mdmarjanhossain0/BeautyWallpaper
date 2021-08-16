package com.appbytes.beautywallpaper.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.appbytes.beautywallpaper.R
import com.takisoft.preferencex.EditTextPreference
import com.takisoft.preferencex.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

        private lateinit var pref: SharedPreferences

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            pref = PreferenceManager.getDefaultSharedPreferences(context)
            initPreference()
        }

        private fun initPreference() {
            val bufferSizePref: ListPreference? = findPreference<ListPreference>(getString(R.string.key_theme))
            /*if (bufferSizePref != null) {
                val bufferSize = pref.getLong(getString(R.string.key_theme), getString(R.string.default_theme))
                bufferSizePref.summary = "$bufferSize MB"
                bufferSizePref.text = bufferSize.toString()
                bindOnPreferenceChanges(bufferSizePref)
            }*/
        }

        override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        private fun bindOnPreferenceChanges(pref: Preference) {
            pref.onPreferenceChangeListener = this
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            return true
        }
    }
}