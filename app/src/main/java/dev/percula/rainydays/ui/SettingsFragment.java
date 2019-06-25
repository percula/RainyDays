package dev.percula.rainydays.ui;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import dev.percula.rainydays.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

}
