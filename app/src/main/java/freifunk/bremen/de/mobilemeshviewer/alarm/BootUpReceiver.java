package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.content.Context;
import android.content.Intent;

import com.google.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import roboguice.receiver.RoboBroadcastReceiver;

public class BootUpReceiver extends RoboBroadcastReceiver {

    @Inject
    private AlarmController alarmController;
    @Inject
    private PreferenceController preferenceController;

    @Override
    protected void handleReceive(Context context, Intent intent) {
        if (preferenceController.isAutostartEnabled()) {
            alarmController.startNodeAlarm();
        }
    }
}