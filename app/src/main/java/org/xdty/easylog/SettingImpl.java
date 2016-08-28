package org.xdty.easylog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingImpl implements Setting {

    private SharedPreferences mPrefs;

    private Context mContext;

    public SettingImpl(Context context) {
        mContext = context.getApplicationContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public boolean isWindowEnabled() {
        return mPrefs.getBoolean(getString(R.string.enable_window_key), false);
    }

    @Override
    public int getWindowAlpha() {
        return mPrefs.getInt(getString(R.string.transparent_key), 40);
    }

    private String getString(int resId) {
        return mContext.getString(resId);
    }
}
