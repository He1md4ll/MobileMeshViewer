package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;
import freifunk.bremen.de.mobilemeshviewer.binding.MeshViewerApp;

public class BootUpReceiver extends BroadcastReceiver {

    @Inject
    AlarmController alarmController;
    @Inject
    PreferenceController preferenceController;

    @Override
    public void onReceive(Context context, Intent intent) {
        ((MeshViewerApp) context).getMeshViewerComponent().inject(this);

        final boolean autoStart = preferenceController.isAutostartEnabled();
        Log.i(this.getClass().getSimpleName(), "Got boot up intent. Auto start: " + autoStart);
        if (autoStart) {
            alarmController.startNodeAlarm();
        }
    }
}