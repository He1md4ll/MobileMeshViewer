package freifunk.bremen.de.mobilemeshviewer.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import freifunk.bremen.de.mobilemeshviewer.PreferenceController;

public class AlarmController {

    private Context context;
    private AlarmManager alarmManager;
    private PreferenceController preferenceController;

    @Inject
    public AlarmController(AlarmManager alarmManager, Context context, PreferenceController preferenceController) {
        this.alarmManager = alarmManager;
        this.context = context;
        this.preferenceController = preferenceController;
    }

    public void startNodeAlarm() {
        Log.i(this.getClass().getSimpleName(), "Starting alarm");
        sendAlarmImmediately();
        changeNodeAlarm();
    }

    public void changeNodeAlarm() {
        final PendingIntent pendingIntent = getPendingIntent();
        final long interval = preferenceController.getAlarmInterval();
        if (interval < 0) {
            stopNodeAlarm();
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, interval, interval, pendingIntent);
            Log.d(this.getClass().getSimpleName(), "Alarm interval set to " + interval + "ms");
        }
    }

    public void stopNodeAlarm() {
        alarmManager.cancel(getPendingIntent());
        Log.i(this.getClass().getSimpleName(), "Alarm stopped");
    }

    public void sendAlarmImmediately() {
        context.sendBroadcast(getAlarmIntent());
    }

    private PendingIntent getPendingIntent() {
        final Intent alarmIntent = getAlarmIntent();
        return PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private Intent getAlarmIntent() {
        return new Intent(context, AlarmReceiver.class);
    }
}