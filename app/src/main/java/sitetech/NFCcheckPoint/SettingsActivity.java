package sitetech.NFCcheckPoint;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import sitetech.NFCcheckPoint.Helpers.ToastHelper;
import sitetech.routecheckapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(androidx.preference.Preference preference) {
            if (preference.getKey().equals("btn_backup"))
                ToastHelper.aviso("CLICK EN BACK UP");

            if (preference.getKey().equals("btn_import"))
                ToastHelper.aviso("CLICK EN IMPORTAR");

            return super.onPreferenceTreeClick(preference);
        }
    }
}