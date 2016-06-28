package freifunk.bremen.de.mobilemeshviewer;


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

import com.google.inject.Inject;

import java.util.Map;

import freifunk.bremen.de.mobilemeshviewer.alarm.AlarmController;
import roboguice.activity.RoboAppCompatPreferenceActivity;

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
public class SettingsActivity extends RoboAppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    private AlarmController alarmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void setupDescription(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> allPrefs = prefs.getAll();

        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            updateSettingsText(entry.getKey());
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSettingsText(key);
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
