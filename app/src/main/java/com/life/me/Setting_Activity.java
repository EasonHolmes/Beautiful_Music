package com.life.me;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.util.Log;


/**
 * Created by cuiyang on 15/10/3.
 */
public class Setting_Activity extends BaseActivity {

    private SettingsFragment mSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        super.initToolbar(getResources().getString(R.string.title_activity_setting), true);
        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    /**
     * A placeholder fragment containing a settings view.
     */
    @SuppressLint("ValidFragment")
    public class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public boolean onPreferenceTreeClick(@NonNull PreferenceScreen preferenceScreen, @NonNull Preference preference) {//判断是哪个控件被点击了
            if ("language".equals(preference.getKey())) {
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("language");
            }
            return true;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.e(getClass().getName(), "ddddd==" + data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(getClass().getName(), "ffffff===" + data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
