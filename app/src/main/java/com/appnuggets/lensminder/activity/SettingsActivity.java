package com.appnuggets.lensminder.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.model.NotificationCode;
import com.appnuggets.lensminder.service.NotificationService;

import java.util.Calendar;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreference darkModeSwitch = (SwitchPreference) findPreference("dark_mode");
            if(null != darkModeSwitch) {
                darkModeSwitch.setOnPreferenceChangeListener((preference, isVibrateOnObject) -> {
                    boolean enableDarkMode = !((SwitchPreference)preference).isChecked();
                    if(enableDarkMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    return true;
                });
            }

            SwitchPreference notificationSwitch = (SwitchPreference) findPreference("notify");
            if(null != notificationSwitch){
                notificationSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                    if(newValue.equals(true)) {
                        // Start notification for everything
                        NotificationService.createNotification(getContext(), 10,
                                NotificationCode.CONTAINER_EXPIRED);
                    }
                    else {
                        // Cancel everything
                        NotificationService.cancelNotification(getContext(),
                                NotificationCode.CONTAINER_EXPIRED);
                    }
                    return true;
                });
            }

        }

    }
}