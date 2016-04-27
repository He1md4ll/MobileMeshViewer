package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        final boolean autoStart = preferenceController.isAutostartEnabled();
        Log.i(this.getClass().getSimpleName(), "Got boot up intent. Auto start: " + autoStart);
        if (autoStart) {
            alarmController.startNodeAlarm();
        }
    }
}