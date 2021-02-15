package com.appnuggets.lensminder.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Container;
import com.appnuggets.lensminder.database.entity.Drops;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.Solution;
import com.appnuggets.lensminder.model.NotificationCode;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.appnuggets.lensminder.service.NotificationService;



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
                        AppDatabase db = AppDatabase.getInstance(getContext());
                        UsageProcessor usageProcessor = new UsageProcessor();

                        Solution solution = db.solutionDao().getInUse();
                        Container container = db.containerDao().getInUse();
                        Drops drops = db.dropsDao().getInUse();
                        Lenses lenses = db.lensesDao().getInUse();

                        if(container != null){
                            NotificationService.createNotification(getContext(),
                                    usageProcessor.calculateUsageLeft(container.startDate,
                                            null, container.useInterval),
                                    NotificationCode.CONTAINER_EXPIRED);
                        }

                        if(solution != null){
                            NotificationService.createNotification(getContext(),
                                    usageProcessor.calculateUsageLeft(solution.startDate,
                                            solution.expirationDate, solution.useInterval),
                                    NotificationCode.SOLUTION_EXPIRED);
                        }

                        if(drops != null){
                            NotificationService.createNotification(getContext(),
                                    usageProcessor.calculateUsageLeft(drops.startDate,
                                            drops.expirationDate, drops.useInterval),
                                    NotificationCode.DROPS_EXPIRED);
                        }

                        if(lenses != null){
                            NotificationService.createNotification(getContext(),
                                    usageProcessor.calculateUsageLeft(lenses.startDate,
                                            lenses.expirationDate, lenses.useInterval),
                                    NotificationCode.LENSES_EXPIRED);
                        }
                    }
                    else {
                        NotificationService.cancelNotification(getContext(),
                                NotificationCode.CONTAINER_EXPIRED);

                        NotificationService.cancelNotification(getContext(),
                                NotificationCode.SOLUTION_EXPIRED);

                        NotificationService.cancelNotification(getContext(),
                                NotificationCode.DROPS_EXPIRED);

                        NotificationService.cancelNotification(getContext(),
                                NotificationCode.LENSES_EXPIRED);
                    }
                    return true;
                });
            }

            PreferenceScreen eyeDefect = (PreferenceScreen) findPreference("eye_defect");
            eyeDefect.setOnPreferenceClickListener(preference -> {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new EyeDefectFragment())
                        .commit();
                return true;
            });


        }

    }

    public static class EyeDefectFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.eye_defect_preferences, rootKey);
        }
    }
}