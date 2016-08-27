package org.xdty.easylog;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.app_name);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFragment())
                    .commit();
        }

    }

    public static class SettingsFragment extends PreferenceFragment
            implements Preference.OnPreferenceClickListener {

        private Map<String, Integer> mKeyMap = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            bindPreference(R.string.enable_window_key);
            bindPreference(R.string.filter_tag_key);
        }

        private void bindPreference(int keyId) {
            String key = getString(keyId);
            mKeyMap.put(key, keyId);
            findPreference(key).setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {

            switch (mKeyMap.get(preference.getKey())) {
                case R.string.enable_window_key:
                    break;
                case R.string.filter_tag_key:
                    break;
                default:
                    break;
            }

            return false;
        }
    }
}
