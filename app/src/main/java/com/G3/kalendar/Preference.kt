package com.G3.kalendar

import android.os.Bundle
import android.preference.PreferenceActivity
import androidx.appcompat.app.AppCompatDelegate

class Preference : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        // turning off night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}