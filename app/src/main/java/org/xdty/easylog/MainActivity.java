package org.xdty.easylog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import java.util.HashMap;
import java.util.Map;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

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

        SharedPreferences sharedPrefs;
        private Map<String, Integer> mKeyMap = new HashMap<>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            sharedPrefs = getPreferenceManager().getSharedPreferences();

            bindPreference(R.string.enable_window_key);
            bindPreference(R.string.filter_tag_key);
            bindPreference(R.string.transparent_key);
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
                    updateWindow();
                    break;
                case R.string.filter_tag_key:
                    break;
                case R.string.transparent_key:
                    showSeekBarDialog(R.string.transparent_key, EasyLogService.WINDOW_TRANS, 40,
                            100, R.string.transparent, R.string.text_transparent);
                    break;
                default:
                    break;
            }

            return false;
        }

        private void updateWindow() {

            boolean enabled = sharedPrefs.getBoolean(getString(R.string.enable_window_key), false);
            Intent serviceIntent = new Intent(getActivity(), EasyLogService.class);
            serviceIntent.putExtra(EasyLogService.ENABLE_WINDOW, enabled);
            getActivity().startService(serviceIntent);
        }

        private void showSeekBarDialog(int keyId, final String bundleKey, int defaultValue,
                int max, int title, int textRes) {
            final String key = getString(keyId);
            int value = sharedPrefs.getInt(key, defaultValue);
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(title));
            View layout = View.inflate(getActivity(), R.layout.dialog_seek, null);
            builder.setView(layout);

            final SeekBarCompat seekBar = (SeekBarCompat) layout.findViewById(R.id.seek_bar);
            seekBar.setMax(max);
            seekBar.setProgress(value);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (progress == 0) {
                        progress = 1;
                    }
                    Intent serviceIntent = new Intent(getActivity(), EasyLogService.class);
                    serviceIntent.putExtra(bundleKey, progress);
                    getActivity().startService(serviceIntent);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int value = seekBar.getProgress();
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putInt(key, value);
                    editor.apply();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.show();
        }
    }
}
