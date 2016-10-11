package freifunk.bremen.de.mobilemeshviewer;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import java.util.Map;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerApp;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    AlarmController alarmController;

    @Inject
    PreferenceController preferenceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MeshViewerApp) getApplication()).getMeshViewerComponent().inject(this);

        addPreferencesFromResource(R.xml.pref_general);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        PreferenceManager.getDefaultSharedPreferences(this);
        setupDescription();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSettingsText(key);
    }

    private void setupDescription(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        findPreference(PreferenceController.PREF_ALARM_INTERVAL).setOnPreferenceChangeListener(buildIntervalChangeListener());

        Map<String, ?> allPrefs = prefs.getAll();
        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            updateSettingsText(entry.getKey());
        }
    }

    private Preference.OnPreferenceChangeListener buildIntervalChangeListener() {
        return new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference pref, final Object newValue) {
                final Long newInterval = Long.valueOf((String) newValue);
                final Long defaultInterval = preferenceController.getDefaultAlarmInterval();

                if (newInterval < defaultInterval) {
                    new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle(R.string.dialog_interval_title)
                            .setMessage(String.format(getString(R.string.dialog_interval_msg), newValue))
                            .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    preferenceController.setAlarmInterval(newValue);
                                }
                            })
                            .setNegativeButton(R.string.dialog_deny, null)
                            .show();
                    return false;
                } else {
                    return true;
                }
            }
        };
    }

    private void updateSettingsText(String key) {
        Preference preference = findPreference(key);

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(preference.getSharedPreferences().getString(key, ""));

            // Set the summary to reflect the new value.
            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : "");

            if (PreferenceController.PREF_ALARM_INTERVAL.equals(key)) {
                alarmController.changeNodeAlarm();
            }
        } else if (preference instanceof RingtonePreference) {
            // For ringtone preferences, look up the correct display value
            // using RingtoneManager.
            Ringtone ringtone = RingtoneManager.getRingtone(
                preference.getContext(), Uri.parse(preference.getSharedPreferences().getString(key, key)));
            if (ringtone == null || ringtone.getTitle(preference.getContext()).equals(preference.getContext().getString(R.string.pref_ringtone_unknown))) {
                // Clear the summary if there was a lookup error.
                preference.setSummary(preference.getContext().getString(R.string.pref_ringtone_silent));
            } else {
                // Set the summary to reflect the new ringtone display name.
                String name = ringtone.getTitle(preference.getContext());
                preference.setSummary(name);
            }
        }
    }
}
